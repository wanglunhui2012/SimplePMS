package indi.simple.pms.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import indi.simple.pms.constant.SystemRedisConstant;
import indi.simple.pms.entity.businessobject.JwtUserBO;
import indi.simple.pms.entity.dataobject.SystemMenuDO;
import indi.simple.pms.entity.dataobject.SystemRoleMenuDO;
import indi.simple.pms.entity.dataobject.SystemUserRoleDO;
import indi.simple.pms.entity.datatransferobject.SystemMenuSearchDTO;
import indi.simple.pms.entity.viewobject.SystemNavigationMenuMetaVO;
import indi.simple.pms.entity.viewobject.SystemNavigationMenuVO;
import indi.simple.pms.exception.BadRequestException;
import indi.simple.pms.mapper.SystemMenuMapper;
import indi.simple.pms.service.SystemMenuService;
import indi.simple.pms.service.SystemRoleMenuService;
import indi.simple.pms.service.SystemUserRoleService;
import indi.simple.pms.service.SystemUserService;
import indi.simple.pms.util.RedisUtil;
import indi.simple.pms.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统菜单表 system_menu 服务实现类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:21
 */
@Service("systemMenuService")
public class SystemMenuServiceImpl extends ServiceImpl<SystemMenuMapper, SystemMenuDO> implements SystemMenuService {
    @Autowired
    private SystemRoleMenuService systemRoleMenuService;
    @Autowired
    private SystemUserRoleService systemUserRoleService;
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean add(SystemMenuDO systemMenuDO) {
        LocalDateTime localDateTime = LocalDateTime.now();
        JwtUserBO jwtUserBO = SecurityUtil.getJwtUserBO();
        if (super.count(Wrappers.<SystemMenuDO>lambdaQuery().eq(SystemMenuDO::getName, systemMenuDO.getName())) > 0) {
            throw new BadRequestException("菜单名已存在!");
        } else {
            systemMenuDO.setCreateUserId(jwtUserBO.getId());
            systemMenuDO.setCreateDepartmentId(jwtUserBO.getDepartmentId());
            systemMenuDO.setCreateTime(localDateTime);
            systemMenuDO.setUpdateTime(localDateTime);
            boolean flag = super.save(systemMenuDO);
            return flag;
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean delete(List<Long> idList) {
        // 要删除的为idList和父级为idList的所有
        List<Long> deleteIdList=this.getChildrenIdListRecursionByIdList(idList);
        deleteIdList.addAll(idList);

        // 必须在删除前获取，否则拿到的为空
        List<Long> roleIdList=this.systemRoleMenuService.list(Wrappers.<SystemRoleMenuDO>lambdaQuery().eq(SystemRoleMenuDO::getMenuId,deleteIdList)).stream().map(SystemRoleMenuDO::getRoleId).collect(Collectors.toList());

        // 删除角色菜单
        this.systemRoleMenuService.remove(Wrappers.<SystemRoleMenuDO>lambdaQuery().in(SystemRoleMenuDO::getMenuId,deleteIdList));
        boolean flag=super.removeByIds(deleteIdList);

        // menu_id->role_id->user_id，再删除缓存
        List<Long> userIdList=this.systemUserRoleService.list(Wrappers.<SystemUserRoleDO>lambdaQuery().in(roleIdList.size()>0,SystemUserRoleDO::getRoleId,roleIdList)).stream().map(SystemUserRoleDO::getUserId).collect(Collectors.toList());
        for (Long userId:userIdList){
            String username=this.systemUserService.getNameById(userId);
            this.redisUtil.keyDelete(SystemRedisConstant.LOGIN_JWTUSER+SystemRedisConstant.SPLIT+username);
        }

        return flag;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean update(SystemMenuDO systemMenuDO) {
        LocalDateTime localDateTime = LocalDateTime.now();
        if (super.count(Wrappers.<SystemMenuDO>lambdaQuery().eq(SystemMenuDO::getName, systemMenuDO.getName()).ne(SystemMenuDO::getId, systemMenuDO.getId())) > 0) {
            throw new BadRequestException("菜单名已存在!");
        } else if (systemMenuDO.getId().equals(systemMenuDO.getParentId())) {
            throw new BadRequestException("上级菜单不能为自己!");
        } else {
            systemMenuDO.setUpdateTime(localDateTime);
            boolean flag = super.updateById(systemMenuDO);

            // 因为JwtUser中缓存的是菜单id，这里不会修改菜单id，不须要修改缓存

            return flag;
        }
    }

    @Override
    public List<SystemMenuDO> searchTree(SystemMenuSearchDTO systemMenuSearchDTO) {
        Set<Long> idSet=new HashSet<>(this.baseMapper.searchIdList(systemMenuSearchDTO));
        // 填充父级
        Set<Long> parentIdSet=new HashSet<>();
        for (Long id:idSet){
            Long parentId=id;
            while(parentId!=0L){
                parentId=this.getParentIdById(parentId);
                parentIdSet.add(parentId);
            }
        }
        idSet.addAll(parentIdSet);

        List<SystemMenuDO> systemMenuDOList=super.listByIds(idSet);
        Map<Long,List<SystemMenuDO>> map=systemMenuDOList.stream().collect(Collectors.groupingBy(SystemMenuDO::getParentId));
        /*Map<Long, List<SystemMenuDO>> map = new LinkedHashMap<>();
        for(int i = 0; i < systemMenuDOList.size(); ++i) {
            SystemMenuDO currentMenu = systemMenuDOList.get(i);
            List<SystemMenuDO> parentChildrenMenuList = map.get(currentMenu.getParentId());
            if (parentChildrenMenuList == null) {
                parentChildrenMenuList = new ArrayList<>();
            }

            parentChildrenMenuList.add(currentMenu);
            map.put(currentMenu.getParentId(), parentChildrenMenuList);
        }*/
        // 设置children
        for (SystemMenuDO systemMenuDO:systemMenuDOList){
            systemMenuDO.setChildren(map.get(systemMenuDO.getId()));
        }

        // 只需要父级id为0的
        return systemMenuDOList.stream().filter(systemMenuDO -> systemMenuDO.getParentId()==0L).collect(Collectors.toList());
    }

    @Override
    public List<SystemNavigationMenuVO> getNavigation() {
        JwtUserBO jwtUserBO = SecurityUtil.getJwtUserBO();
        List<SystemMenuDO> systemMenuDOList = super.baseMapper.getByUserId(jwtUserBO.getId());
        if (systemMenuDOList == null) {
            return new ArrayList<>();
        } else {
            Map<Long,List<SystemMenuDO>> map=systemMenuDOList.stream().collect(Collectors.groupingBy(SystemMenuDO::getParentId));
            /*Map<Long, List<SystemMenuDO>> map = new LinkedHashMap<>();
            for(int i = 0; i < systemMenuDOList.size(); ++i) {
                SystemMenuDO currentMenu = systemMenuDOList.get(i);
                List<SystemMenuDO> parentChildrenMenuList = map.get(currentMenu.getParentId());
                if (parentChildrenMenuList == null) {
                    parentChildrenMenuList = new ArrayList<>();
                }

                parentChildrenMenuList.add(currentMenu);
                map.put(currentMenu.getParentId(), parentChildrenMenuList);
            }*/
            // 设置children
            for (SystemMenuDO systemMenuDO:systemMenuDOList){
                systemMenuDO.setChildren(map.get(systemMenuDO.getId()));
            }

            // 只需要父级id为0的
            List<SystemMenuDO> temp=systemMenuDOList.stream().filter(systemMenuDO -> systemMenuDO.getParentId()==0L).collect(Collectors.toList());

            return this.systemMenuDOListToSystemNavigationMenuVOList(temp);
        }
    }
    public List<SystemNavigationMenuVO> systemMenuDOListToSystemNavigationMenuVOList(List<SystemMenuDO> systemMenuDOList){
        List<SystemNavigationMenuVO> systemNavigationMenuVOList=systemMenuDOList.stream().map(systemMenuDO -> {
            SystemNavigationMenuVO systemNavigationMenuVO = new SystemNavigationMenuVO();
            systemNavigationMenuVO.setName(systemMenuDO.getName());
            systemNavigationMenuVO.setPath(systemMenuDO.getPath());
            systemNavigationMenuVO.setComponent(systemMenuDO.getComponent());
            systemNavigationMenuVO.setRedirect(systemMenuDO.getRedirect());
            systemNavigationMenuVO.setHidden("1".equals(systemMenuDO.getHidden()));
            systemNavigationMenuVO.setAlwaysShow("1".equals(systemMenuDO.getAlwaysShow()));
            SystemNavigationMenuMetaVO systemNavigationMenuMetaVO = new SystemNavigationMenuMetaVO();
            systemNavigationMenuMetaVO.setTitle(systemMenuDO.getName());
            systemNavigationMenuMetaVO.setIcon(systemMenuDO.getIcon());
            systemNavigationMenuMetaVO.setNoCache("1".equals(systemMenuDO.getNoCache()));
            systemNavigationMenuMetaVO.setBreadcrumb("1".equals(systemMenuDO.getBreadcrumb()));
            systemNavigationMenuMetaVO.setAffix("1".equals(systemMenuDO.getAffix()));
            systemNavigationMenuVO.setMeta(systemNavigationMenuMetaVO);
            if(systemMenuDO.getChildren()!=null){
                systemNavigationMenuVO.setChildren(systemMenuDOListToSystemNavigationMenuVOList(systemMenuDO.getChildren()));
            }

            return systemNavigationMenuVO;
        }).collect(Collectors.toList());

        return systemNavigationMenuVOList;
    }

    /**
     * 获取祖先有id的id列表，只获取一层
     */
    @Override
    public List<Long> getChildrenIdListById(Long id) {
        return super.baseMapper.getChildrenIdListById(id);
    }
    /**
     * 获取祖先有idList的id列表，递归获取多层
     */
    @Override
    public List<Long> getChildrenIdListRecursionByIdList(List<Long> idList) {
        List<Long> result=new ArrayList<>();

        LinkedList<Long> stack=new LinkedList<>(idList);
        while(!stack.isEmpty()){
            int size=stack.size();
            for (int i=0;i<size;i++){
                Long currentId=stack.pop();
                List<Long> currentChildrenIdList=this.getChildrenIdListById(currentId);
                result.addAll(currentChildrenIdList);
                stack.addAll(currentChildrenIdList);
            }
        }

        return result;
    }
    /**
     * 通过id获取父级id
     */
    @Override
    public Long getParentIdById(Long id) {
        return super.baseMapper.getParentIdById(id);
    }
}

package indi.simple.pms.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import indi.simple.pms.constant.SystemRedisConstant;
import indi.simple.pms.entity.businessobject.JwtUserBO;
import indi.simple.pms.entity.dataobject.SystemDepartmentDO;
import indi.simple.pms.entity.dataobject.SystemRoleDepartmentDO;
import indi.simple.pms.entity.dataobject.SystemUserDO;
import indi.simple.pms.entity.datatransferobject.SystemDepartmentSearchDTO;
import indi.simple.pms.exception.BadRequestException;
import indi.simple.pms.mapper.SystemDepartmentMapper;
import indi.simple.pms.service.SystemDepartmentService;
import indi.simple.pms.service.SystemRoleDepartmentService;
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
 * 系统部门表 system_department 服务实现类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:11
 */
@Service("systemDepartmentService")
public class SystemDepartmentServiceImpl extends ServiceImpl<SystemDepartmentMapper, SystemDepartmentDO> implements SystemDepartmentService {
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private SystemRoleDepartmentService systemRoleDepartmentService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean add(SystemDepartmentDO systemDepartmentDO) {
        LocalDateTime localDateTime = LocalDateTime.now();
        JwtUserBO jwtUserBO = SecurityUtil.getJwtUserBO();
        if (super.count(Wrappers.<SystemDepartmentDO>lambdaQuery().eq(SystemDepartmentDO::getName, systemDepartmentDO.getName())) > 0) {
            throw new BadRequestException("部门名已存在!");
        } else {
            systemDepartmentDO.setCreateUserId(jwtUserBO.getId());
            systemDepartmentDO.setCreateDepartmentId(jwtUserBO.getDepartmentId());
            systemDepartmentDO.setCreateTime(localDateTime);
            systemDepartmentDO.setUpdateTime(localDateTime);
            boolean flag = super.save(systemDepartmentDO);
            return flag;
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean delete(List<Long> idList) {
        // 要删除的为idList和父级为idList的所有
        List<Long> deleteIdList=this.getChildrenIdListRecursionByIdList(idList);
        deleteIdList.addAll(idList);

        // 必须先获取，否则删除后拿到的为空
        List<String> usernameList=this.systemUserService.getNameListByDepartmentId(idList);

        // 置用户表部门id为null
        this.systemUserService.update(Wrappers.<SystemUserDO>lambdaUpdate().in(SystemUserDO::getDepartmentId, deleteIdList).set(SystemUserDO::getDepartmentId, null));
        // 最后删除角色部门
        this.systemRoleDepartmentService.remove(Wrappers.<SystemRoleDepartmentDO>lambdaQuery().in(SystemRoleDepartmentDO::getDepartmentId, deleteIdList));
        boolean flag=super.removeByIds(deleteIdList);

        for (String username:usernameList){
            this.redisUtil.keyDelete(SystemRedisConstant.LOGIN_JWTUSER+SystemRedisConstant.SPLIT+username);
        }

        return flag;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean update(SystemDepartmentDO systemDepartmentDO) {
        LocalDateTime localDateTime = LocalDateTime.now();
        if (super.count(Wrappers.<SystemDepartmentDO>lambdaQuery().eq(SystemDepartmentDO::getName, systemDepartmentDO.getName()).ne(SystemDepartmentDO::getId, systemDepartmentDO.getId())) > 0) {
            throw new BadRequestException("部门名已存在!");
        } else if (systemDepartmentDO.getId().equals(systemDepartmentDO.getParentId())) {
            throw new BadRequestException("上级部门不能是自己!");
        } else {
            systemDepartmentDO.setUpdateTime(localDateTime);
            boolean flag = super.updateById(systemDepartmentDO);

            // JwtUser缓存中只有部门id，修改不好修改部门id，所以不需要删除缓存

            return flag;
        }
    }

    @Override
    public List<SystemDepartmentDO> searchTree(SystemDepartmentSearchDTO departmentSearchDTO) {
        Set<Long> idSet=new HashSet<>(this.baseMapper.searchIdList(departmentSearchDTO));
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

        List<SystemDepartmentDO> systemDepartmentDOList=super.listByIds(idSet);

        Map<Long,List<SystemDepartmentDO>> map=systemDepartmentDOList.stream().collect(Collectors.groupingBy(SystemDepartmentDO::getParentId));
        /*Map<Long, List<SystemDepartmentDO>> map = new LinkedHashMap<>();
        for(int i = 0; i < systemDepartmentDOList.size(); ++i) {
            SystemDepartmentDO currentDepartment = systemDepartmentDOList.get(i);
            List<SystemDepartmentDO> parentChildrenDepartmentList = map.get(currentDepartment.getParentId());
            if (parentChildrenDepartmentList == null) {
                parentChildrenDepartmentList = new ArrayList<>();
            }

            parentChildrenDepartmentList.add(currentDepartment);
            map.put(currentDepartment.getParentId(), parentChildrenDepartmentList);
        }*/
        // 设置children
        for (SystemDepartmentDO systemDepartmentDO:systemDepartmentDOList){
            systemDepartmentDO.setChildren(map.get(systemDepartmentDO.getId()));
        }

        // 只需要父级id为0的
        return systemDepartmentDOList.stream().filter(systemDepartmentDO -> systemDepartmentDO.getParentId()==0L).collect(Collectors.toList());
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

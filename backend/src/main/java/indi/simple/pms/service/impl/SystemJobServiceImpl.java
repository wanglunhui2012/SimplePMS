package indi.simple.pms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import indi.simple.pms.entity.businessobject.JwtUserBO;
import indi.simple.pms.entity.dataobject.SystemJobDO;
import indi.simple.pms.entity.dataobject.SystemUserJobDO;
import indi.simple.pms.entity.datatransferobject.SystemJobSearchDTO;
import indi.simple.pms.exception.BadRequestException;
import indi.simple.pms.mapper.SystemJobMapper;
import indi.simple.pms.service.SystemJobService;
import indi.simple.pms.service.SystemUserJobService;
import indi.simple.pms.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 系统职位表 system_job 服务实现类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:17
 */
@Service("systemJobService")
public class SystemJobServiceImpl extends ServiceImpl<SystemJobMapper, SystemJobDO> implements SystemJobService {
    @Autowired
    private SystemUserJobService systemUserJobService;
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(SystemJobDO systemJobDO) {
        LocalDateTime localDateTime = LocalDateTime.now();
        JwtUserBO jwtUserBO = SecurityUtil.getJwtUserBO();
        if (super.count(Wrappers.<SystemJobDO>lambdaQuery().eq(SystemJobDO::getName, systemJobDO.getName())) > 0) {
            throw new BadRequestException("职位名已存在!");
        } else {
            systemJobDO.setCreateUserId(jwtUserBO.getId());
            systemJobDO.setCreateDepartmentId(jwtUserBO.getDepartmentId());
            systemJobDO.setCreateTime(localDateTime);
            systemJobDO.setUpdateTime(localDateTime);
            boolean flag = super.save(systemJobDO);
            return flag;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(List<Long> idList) {
        this.systemUserJobService.remove(Wrappers.<SystemUserJobDO>lambdaQuery().in(SystemUserJobDO::getJobId, idList));
        boolean flag = super.removeByIds(idList);
        // JwtUser中缓存的没有职位信息，不需要修改缓存
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(SystemJobDO systemJobDO) {
        LocalDateTime localDateTime = LocalDateTime.now();
        if (super.count(Wrappers.<SystemJobDO>lambdaQuery().eq(SystemJobDO::getName, systemJobDO.getName()).ne(SystemJobDO::getId, systemJobDO.getId())) > 0) {
            throw new BadRequestException("职位名已存在!");
        } else {
            systemJobDO.setUpdateTime(localDateTime);
            boolean flag = super.updateById(systemJobDO);
            // JwtUser中缓存的没有职位信息，不需要修改缓存
            return flag;
        }
    }

    @Override
    public List<SystemJobDO> getByUserId(Long userId) {
        return super.baseMapper.getByUserId(userId);
    }

    @Override
    public List<SystemJobDO> getByUserIdList(List<Long> userIdList) {
        return super.baseMapper.getByUserIdList(userIdList);
    }

    @Override
    public IPage<SystemJobDO> searchPage(SystemJobSearchDTO systemJobSearchDTO) {
        Long pageNum = Optional.ofNullable(systemJobSearchDTO.getPageNum()).orElse(0L);
        Long pageSize = Optional.ofNullable(systemJobSearchDTO.getPageSize()).orElse(-1L);

        IPage<SystemJobDO> systemJobDOIPage = super.baseMapper.searchPage(new Page<>(pageNum, pageSize), systemJobSearchDTO);
        return systemJobDOIPage;
    }

    @Override
    public List<Long> getCurrentJobIdList() {
        JwtUserBO jwtUserBO = SecurityUtil.getJwtUserBO();

        return this.getJobIdListByUserId(jwtUserBO.getId());
    }

    @Override
    public List<Long> getJobIdListByUserId(Long userId) {
        return super.baseMapper.getJobIdListByUserId(userId);
    }
}

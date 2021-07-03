package indi.simple.pms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import indi.simple.pms.entity.businessobject.JwtUserBO;
import indi.simple.pms.entity.dataobject.SystemDictionaryDO;
import indi.simple.pms.entity.dataobject.SystemDictionaryDetailDO;
import indi.simple.pms.entity.datatransferobject.SystemDictionarySearchDTO;
import indi.simple.pms.exception.BadRequestException;
import indi.simple.pms.mapper.SystemDictionaryMapper;
import indi.simple.pms.service.SystemDictionaryDetailService;
import indi.simple.pms.service.SystemDictionaryService;
import indi.simple.pms.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 系统字典表 system_dictionary 服务实现类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:13
 */
@Service("systemDictionaryService")
public class SystemDictionaryServiceImpl extends ServiceImpl<SystemDictionaryMapper, SystemDictionaryDO> implements SystemDictionaryService {
    @Autowired
    private SystemDictionaryDetailService systemDictionaryDetailService;

    @Transactional(rollbackFor = {Exception.class})
    public boolean add(SystemDictionaryDO systemDictionaryDO) {
        LocalDateTime localDateTime = LocalDateTime.now();
        JwtUserBO jwtUserBO = SecurityUtil.getJwtUserBO();
        if (super.count(Wrappers.<SystemDictionaryDO>lambdaQuery().eq(SystemDictionaryDO::getName, systemDictionaryDO.getName())) > 0) {
            throw new BadRequestException("字典名已存在!");
        } else {
            systemDictionaryDO.setCreateUserId(jwtUserBO.getId());
            systemDictionaryDO.setCreateDepartmentId(jwtUserBO.getDepartmentId());
            systemDictionaryDO.setCreateTime(localDateTime);
            systemDictionaryDO.setUpdateTime(localDateTime);
            boolean flag = super.save(systemDictionaryDO);
            return flag;
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean delete(List<Long> idList) {
        this.systemDictionaryDetailService.remove(Wrappers.<SystemDictionaryDetailDO>lambdaQuery().in(SystemDictionaryDetailDO::getDictionaryId, idList));
        return super.removeByIds(idList);
    }

    @Transactional(rollbackFor = {Exception.class})
    public boolean update(SystemDictionaryDO systemDictionaryDO) {
        LocalDateTime localDateTime = LocalDateTime.now();
        if (super.count(Wrappers.<SystemDictionaryDO>lambdaQuery().eq(SystemDictionaryDO::getName, systemDictionaryDO.getName()).ne(SystemDictionaryDO::getId, systemDictionaryDO.getId())) > 0) {
            throw new BadRequestException("字典名已存在!");
        } else {
            systemDictionaryDO.setUpdateTime(localDateTime);
            boolean flag = super.updateById(systemDictionaryDO);
            return flag;
        }
    }

    public IPage<SystemDictionaryDO> searchPage(SystemDictionarySearchDTO systemDictionarySearchDTO) {
        Long pageNum = Optional.ofNullable(systemDictionarySearchDTO.getPageNum()).orElse(0L);
        Long pageSize = Optional.ofNullable(systemDictionarySearchDTO.getPageSize()).orElse(-1L);
        IPage<SystemDictionaryDO> dictionaryDOIPage = super.baseMapper.searchPage(new Page<>(pageNum, pageSize), systemDictionarySearchDTO);
        return dictionaryDOIPage;
    }
}

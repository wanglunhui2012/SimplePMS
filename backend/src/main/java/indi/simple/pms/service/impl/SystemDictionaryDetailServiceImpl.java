package indi.simple.pms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import indi.simple.pms.entity.businessobject.JwtUserBO;
import indi.simple.pms.entity.dataobject.SystemDictionaryDO;
import indi.simple.pms.entity.dataobject.SystemDictionaryDetailDO;
import indi.simple.pms.entity.datatransferobject.SystemDictionaryDetailSearchDTO;
import indi.simple.pms.entity.viewobject.SystemDictionaryDetailVO;
import indi.simple.pms.exception.BadRequestException;
import indi.simple.pms.mapper.SystemDictionaryDetailMapper;
import indi.simple.pms.service.SystemDictionaryDetailService;
import indi.simple.pms.service.SystemDictionaryService;
import indi.simple.pms.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 系统字典详情表 system_dictionary_detail 服务实现类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:15
 */
@Service("systemDictionaryDetailService")
public class SystemDictionaryDetailServiceImpl extends ServiceImpl<SystemDictionaryDetailMapper, SystemDictionaryDetailDO> implements SystemDictionaryDetailService {
    @Autowired
    private SystemDictionaryService systemDictionaryService;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean add(SystemDictionaryDetailDO systemDictionaryDetailDO) {
        LocalDateTime localDateTime = LocalDateTime.now();
        JwtUserBO jwtUserBO = SecurityUtil.getJwtUserBO();
        if (this.systemDictionaryService.count(Wrappers.<SystemDictionaryDO>lambdaQuery().eq(SystemDictionaryDO::getId, systemDictionaryDetailDO.getDictionaryId())) < 1) {
            throw new BadRequestException("字典id不存在!");
        } else if (super.count(Wrappers.<SystemDictionaryDetailDO>lambdaQuery().eq(SystemDictionaryDetailDO::getDictionaryId, systemDictionaryDetailDO.getDictionaryId()).eq(SystemDictionaryDetailDO::getKey, systemDictionaryDetailDO.getKey())) > 0) {
            throw new BadRequestException("字典详情key已存在!");
        } else {
            systemDictionaryDetailDO.setCreateUserId(jwtUserBO.getId());
            systemDictionaryDetailDO.setCreateDepartmentId(jwtUserBO.getDepartmentId());
            systemDictionaryDetailDO.setCreateTime(localDateTime);
            systemDictionaryDetailDO.setUpdateTime(localDateTime);
            boolean flag = super.save(systemDictionaryDetailDO);
            return flag;
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean delete(List<Long> idList) {
        return super.removeByIds(idList);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean update(SystemDictionaryDetailDO systemDictionaryDetailDO) {
        LocalDateTime localDateTime = LocalDateTime.now();
        if (this.systemDictionaryService.count(Wrappers.<SystemDictionaryDO>lambdaQuery().eq(SystemDictionaryDO::getId, systemDictionaryDetailDO.getDictionaryId())) < 1) {
            throw new BadRequestException("字典id不存在!");
        } else if (super.count(Wrappers.<SystemDictionaryDetailDO>lambdaQuery().eq(SystemDictionaryDetailDO::getDictionaryId, systemDictionaryDetailDO.getDictionaryId()).eq(SystemDictionaryDetailDO::getKey, systemDictionaryDetailDO.getKey()).ne(SystemDictionaryDetailDO::getId, systemDictionaryDetailDO.getId())) > 0) {
            throw new BadRequestException("字典详情key已存在!");
        } else {
            systemDictionaryDetailDO.setUpdateTime(localDateTime);
            boolean flag = super.updateById(systemDictionaryDetailDO);
            return flag;
        }
    }

    @Override
    public IPage<SystemDictionaryDetailDO> searchPage(SystemDictionaryDetailSearchDTO systemDictionaryDetailSearchDTO) {
        Long pageNum = Optional.ofNullable(systemDictionaryDetailSearchDTO.getPageNum()).orElse(0L);
        Long pageSize = Optional.ofNullable(systemDictionaryDetailSearchDTO.getPageSize()).orElse(-1L);
        IPage<SystemDictionaryDetailDO> dictionaryDetailDOIPage = super.baseMapper.searchPage(new Page<>(pageNum, pageSize), systemDictionaryDetailSearchDTO);
        return dictionaryDetailDOIPage;
    }

    @Override
    public Map<String, Map<String,String>> searchMap(List<String> nameList) {
        List<SystemDictionaryDO> systemDictionaryDOList;
        if(nameList==null||nameList.size()==0){
            systemDictionaryDOList=systemDictionaryService.list();
        }else{
            systemDictionaryDOList=systemDictionaryService.list(Wrappers.<SystemDictionaryDO>lambdaQuery().in(SystemDictionaryDO::getName,nameList));
        }

        Map<String, Map<String,String>> result=new HashMap<>();
        for (SystemDictionaryDO systemDictionaryDO:systemDictionaryDOList){
            List<SystemDictionaryDetailVO> systemDictionaryDetailVOList=this.getSystemDictionaryDetailVOByDictionaryId(systemDictionaryDO.getId());
            for (SystemDictionaryDetailVO systemDictionaryDetailVO:systemDictionaryDetailVOList){
                result.computeIfAbsent(systemDictionaryDO.getName(),k->new HashMap<>()).put(systemDictionaryDetailVO.getKey(),systemDictionaryDetailVO.getValue());
            }
        }

        return result;
    }

    @Override
    public List<SystemDictionaryDetailVO>  getSystemDictionaryDetailVOByDictionaryId(Long dictionaryId) {
        return super.baseMapper.getSystemDictionaryDetailVOByDictionaryId(dictionaryId);
    }
}

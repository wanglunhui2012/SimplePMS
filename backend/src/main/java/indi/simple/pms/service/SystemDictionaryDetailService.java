package indi.simple.pms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import indi.simple.pms.entity.dataobject.SystemDictionaryDetailDO;
import indi.simple.pms.entity.datatransferobject.SystemDictionaryDetailSearchDTO;
import indi.simple.pms.entity.viewobject.SystemDictionaryDetailVO;

import java.util.List;
import java.util.Map;

/**
 * 系统字典详情表 system_dictionary_detail 服务类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:15
 */
public interface SystemDictionaryDetailService extends IService<SystemDictionaryDetailDO> {
    boolean add(SystemDictionaryDetailDO systemDictionaryDetailDO);

    boolean delete(List<Long> idList);

    boolean update(SystemDictionaryDetailDO systemDictionaryDetailDO);

    IPage<SystemDictionaryDetailDO> searchPage(SystemDictionaryDetailSearchDTO systemDictionaryDetailSearchDTO);

    Map<String, Map<String,String>> searchMap(List<String> nameList);

    List<SystemDictionaryDetailVO> getSystemDictionaryDetailVOByDictionaryId(Long dictionaryId);
}

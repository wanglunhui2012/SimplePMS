package indi.simple.pms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import indi.simple.pms.entity.dataobject.SystemDictionaryDO;
import indi.simple.pms.entity.datatransferobject.SystemDictionarySearchDTO;

import java.util.List;

/**
 * 系统字典表 system_dictionary 服务类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:13
 */
public interface SystemDictionaryService extends IService<SystemDictionaryDO> {
    boolean add(SystemDictionaryDO systemDictionaryDO);

    boolean delete(List<Long> idList);

    boolean update(SystemDictionaryDO systemDictionaryDO);

    IPage<SystemDictionaryDO> searchPage(SystemDictionarySearchDTO systemDictionarySearchDTO);
}

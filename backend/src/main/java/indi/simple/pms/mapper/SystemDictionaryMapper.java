package indi.simple.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import indi.simple.pms.entity.dataobject.SystemDictionaryDO;
import indi.simple.pms.entity.datatransferobject.SystemDictionarySearchDTO;
import org.apache.ibatis.annotations.Param;

/**
 * 系统字典表 system_dictionary 数据库映射类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:13
 */
public interface SystemDictionaryMapper extends BaseMapper<SystemDictionaryDO> {

    IPage<SystemDictionaryDO> searchPage(Page<SystemDictionaryDO> page, @Param("systemDictionarySearchDTO") SystemDictionarySearchDTO systemDictionarySearchDTO);

}

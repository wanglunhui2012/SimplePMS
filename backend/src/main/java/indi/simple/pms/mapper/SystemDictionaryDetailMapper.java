package indi.simple.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import indi.simple.pms.entity.dataobject.SystemDictionaryDetailDO;
import indi.simple.pms.entity.datatransferobject.SystemDictionaryDetailSearchDTO;
import indi.simple.pms.entity.viewobject.SystemDictionaryDetailVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统字典详情表 system_dictionary_detail 数据库映射类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:15
 */
public interface SystemDictionaryDetailMapper extends BaseMapper<SystemDictionaryDetailDO> {

    IPage<SystemDictionaryDetailDO> searchPage(Page<SystemDictionaryDetailDO> page, @Param("systemDictionaryDetailSearchDTO") SystemDictionaryDetailSearchDTO systemDictionaryDetailSearchDTO);

    @Select("select `key`,value from system_dictionary_detail where dictionary_id=#{dictionaryId} order by sort asc")
    List<SystemDictionaryDetailVO> getSystemDictionaryDetailVOByDictionaryId(@Param("dictionaryId") Long dictionaryId);
}

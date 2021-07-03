package indi.simple.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import indi.simple.pms.entity.dataobject.SystemLogDO;
import indi.simple.pms.entity.datatransferobject.SystemLogSearchDTO;
import org.apache.ibatis.annotations.Param;

/**
 * 系统日志表 system_log 数据库映射类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:19
 */
public interface SystemLogMapper extends BaseMapper<SystemLogDO> {
    int getIPCount(@Param("beginDateTime") String beginDateTime,@Param("endDateTime") String endDateTime);

    int getPageCount(@Param("beginDateTime") String beginDateTime,@Param("endDateTime") String endDateTime);

    IPage<SystemLogDO> searchPage(Page<SystemLogDO> page, @Param("systemLogSearchDTO") SystemLogSearchDTO systemLogSearchDTO);

}

package indi.simple.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import indi.simple.pms.entity.dataobject.SystemDepartmentDO;
import indi.simple.pms.entity.datatransferobject.SystemDepartmentSearchDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统部门表 system_department 数据库映射类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:10
 */
public interface SystemDepartmentMapper extends BaseMapper<SystemDepartmentDO> {

    List<Long> searchIdList(@Param("systemDepartmentSearchDTO") SystemDepartmentSearchDTO systemDepartmentSearchDTO);

    @Select("select id from system_department where parent_id=#{id}")
    List<Long> getChildrenIdListById(@Param("id") Long id);

    @Select("select parent_id from system_department where id=#{id}")
    Long getParentIdById(@Param("id") Long id);

}

package indi.simple.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import indi.simple.pms.entity.dataobject.SystemMenuDO;
import indi.simple.pms.entity.datatransferobject.SystemMenuSearchDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统菜单表 system_menu 数据库映射类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:49:46
 */
public interface SystemMenuMapper extends BaseMapper<SystemMenuDO> {
    @Select("select distinct m.id,m.name,m.parent_id,m.sort,m.path,m.component,m.icon,m.redirect,m.hidden,m.always_show,m.no_cache,m.breadcrumb,m.affix,m.remark,m.create_user_id,m.create_department_id,m.create_time,m.update_time from system_menu m " +
            "left join system_role_menu rm on rm.menu_id=m.id " +
            "left join system_user_role ur on ur.role_id=rm.role_id " +
            "left join system_user u on ur.user_id=u.id " +
            "where u.id=#{userId} " +
            "order by m.sort")
    List<SystemMenuDO> getByUserId(@Param("userId") Long userId);

    List<Long> searchIdList(@Param("systemMenuSearchDTO") SystemMenuSearchDTO systemMenuSearchDTO);

    @Select("select id from system_menu where parent_id=#{id}")
    List<Long> getChildrenIdListById(@Param("id") Long id);

    @Select("select parent_id from system_menu where id=#{id}")
    Long getParentIdById(@Param("id") Long id);
}

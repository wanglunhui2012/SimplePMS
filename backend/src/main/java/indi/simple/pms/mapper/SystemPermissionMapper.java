package indi.simple.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import indi.simple.pms.entity.dataobject.SystemPermissionDO;
import indi.simple.pms.entity.datatransferobject.SystemPermissionSearchDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统权限表 system_permission 数据库映射类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:49:59
 */
public interface SystemPermissionMapper extends BaseMapper<SystemPermissionDO> {

    IPage<SystemPermissionDO> searchPage(Page<SystemPermissionDO> page, @Param("systemPermissionSearchDTO") SystemPermissionSearchDTO systemPermissionSearchDTO);

    @Select({"select distinct p.name from system_permission p \nleft join system_role_permission rp on p.id=rp.permission_id\nleft join system_user_role ur on rp.role_id=ur.role_id\nwhere ur.user_id=#{userId}"})
    List<String> getPermissionByUserId(@Param("userId") Long userId);

    @Select("select name from system_permission where id=#{permissionId}")
    String getNameById(@Param("permissionId") Long permissionId);

}

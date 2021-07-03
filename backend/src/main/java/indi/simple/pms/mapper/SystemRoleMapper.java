package indi.simple.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import indi.simple.pms.entity.dataobject.SystemRoleDO;
import indi.simple.pms.entity.datatransferobject.SystemRoleSearchDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统角色表 system_role 数据库映射类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:50:13
 */
public interface SystemRoleMapper extends BaseMapper<SystemRoleDO> {
    @Select({"select distinct r.id,r.`name`,r.data_scope,r.remark,r.create_user_id,r.create_department_id,r.create_time,r.update_time from system_role r\nleft join system_user_role ur on ur.role_id = r.id \nleft join system_user u on u.id = ur.user_id \nwhere u.id=#{userId}"})
    List<SystemRoleDO> getByUserId(Long var1);

    IPage<SystemRoleDO> searchPage(Page<SystemRoleDO> page, @Param("systemRoleSearchDTO") SystemRoleSearchDTO systemRoleSearchDTO);

    @Select("select role_id from system_user_role where user_id=#{userId}")
    List<Long> getRoleIdListByUserId(@Param("userId") Long userId);
}

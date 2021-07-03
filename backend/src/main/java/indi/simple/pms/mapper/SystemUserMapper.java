package indi.simple.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import indi.simple.pms.entity.dataobject.SystemUserDO;
import indi.simple.pms.entity.datatransferobject.SystemUserSearchDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统用户表 system_user 数据库映射类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:32
 */
public interface SystemUserMapper extends BaseMapper<SystemUserDO> {
    IPage<SystemUserDO> searchPage(Page<SystemUserDO> page, @Param("systemUserSearchDTO") SystemUserSearchDTO systemUserSearchDTO);

    @Update({"update system_user set password=#{newPassword},update_time=#{localDateTime} where id=#{userId}"})
    int changePassword(@Param("newPassword") String newPassword, @Param("localDateTime") LocalDateTime localDateTime, @Param("userId") Long userId);

    @Select({"select distinct u.id,u.`name`,u.nick_name,u.`password`,u.salt,u.avatar,u.`enable`,u.email,u.phone_number,u.sex,u.remark,u.department_id,u.create_user_id,u.create_department_id,u.create_time,u.update_time from system_job j\nleft join system_user_job uj on uj.job_id=j.id\nleft join system_user u on uj.user_id=u.id\nwhere j.id=#{jobId}"})
    List<SystemUserDO> getByJobId(Long var1);

    @Select("select avatar_url from system_user where id=#{userId}")
    String getAvatarUrl(@Param("userId") Long userId);

    @Select("select name from system_user where id=#{userId}")
    String getNameById(@Param("userId") Long userId);

    List<String> getNameListByDepartmentId(@Param("departmentIdList") List<Long> departmentIdList);
}
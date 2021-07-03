package indi.simple.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import indi.simple.pms.entity.dataobject.SystemJobDO;
import indi.simple.pms.entity.datatransferobject.SystemJobSearchDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统职位表 system_job 数据库映射类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:49:31
 */
public interface SystemJobMapper extends BaseMapper<SystemJobDO> {
    @Select({"select distinct j.id,j.`code`,j.`name`,j.remark,j.create_user_id,j.create_department_id,j.create_time,j.update_time from system_job j\nleft join system_user_job uj on uj.job_id=j.id\nleft join system_user u on uj.user_id=u.id\nwhere u.id=#{userId}"})
    List<SystemJobDO> getByUserId(@Param("userId") Long userId);

    List<SystemJobDO> getByUserIdList(@Param("userIdList") List<Long> userIdList);

    IPage<SystemJobDO> searchPage(Page<SystemJobDO> page, @Param("systemJobSearchDTO") SystemJobSearchDTO systemJobSearchDTO);

    @Select("select job_id from system_user_job where user_id=#{userId}")
    List<Long> getJobIdListByUserId(@Param("userId") Long userId);
}

package indi.simple.pms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import indi.simple.pms.entity.dataobject.SystemJobDO;
import indi.simple.pms.entity.datatransferobject.SystemJobSearchDTO;

import java.util.List;

/**
 * 系统职位表 system_job 服务类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:17
 */
public interface SystemJobService extends IService<SystemJobDO> {
    boolean add(SystemJobDO systemJobDO);

    boolean delete(List<Long> idList);

    boolean update(SystemJobDO systemJobDO);

    IPage<SystemJobDO> searchPage(SystemJobSearchDTO systemJobSearchDTO);

    List<SystemJobDO> getByUserId(Long userId);

    List<SystemJobDO> getByUserIdList(List<Long> userIdList);

    List<Long> getCurrentJobIdList();

    List<Long> getJobIdListByUserId(Long userId);
}

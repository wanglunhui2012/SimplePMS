package indi.simple.pms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import indi.simple.pms.entity.dataobject.SystemUserJobDO;
import indi.simple.pms.mapper.SystemUserJobMapper;
import indi.simple.pms.service.SystemUserJobService;
import org.springframework.stereotype.Service;

/**
 * 系统用户职位关联表 system_user_job 服务实现类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:37
 */
@Service("systemUserJobService")
public class SystemUserJobServiceImpl extends ServiceImpl<SystemUserJobMapper, SystemUserJobDO> implements SystemUserJobService {
}

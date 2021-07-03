package indi.simple.pms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import indi.simple.pms.entity.dataobject.SystemRoleDO;
import indi.simple.pms.entity.datatransferobject.SystemRoleSearchDTO;

import java.util.List;

/**
 * 系统角色表 system_role 服务类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:25
 */
public interface SystemRoleService extends IService<SystemRoleDO> {
    boolean add(SystemRoleDO systemRoleDO);

    boolean delete(List<Long> idList);

    boolean update(SystemRoleDO systemRoleDO);

    IPage<SystemRoleDO> searchPage(SystemRoleSearchDTO systemRoleSearchDTO);

    List<SystemRoleDO> getByUserId(Long userId);

    List<Long> getCurrentRoleIdList();

    List<Long> getRoleIdListByUserId(Long userId);
}

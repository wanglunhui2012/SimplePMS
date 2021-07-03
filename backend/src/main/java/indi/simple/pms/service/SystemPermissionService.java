package indi.simple.pms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import indi.simple.pms.entity.dataobject.SystemPermissionDO;
import indi.simple.pms.entity.datatransferobject.SystemPermissionSearchDTO;

import java.util.List;

/**
 * 系统权限表 system_permission 服务类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:23
 */
public interface SystemPermissionService extends IService<SystemPermissionDO> {
    boolean add(SystemPermissionDO systemPermissionDO);

    boolean delete(List<Long> idList);

    boolean update(SystemPermissionDO systemPermissionDO);

    IPage<SystemPermissionDO> searchPage(SystemPermissionSearchDTO systemPermissionSearchDTO);

    List<String> getPermissionByUserId(Long userId);

    String getNameById(Long permissionId);
}

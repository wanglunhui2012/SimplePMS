package indi.simple.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import indi.simple.pms.entity.dataobject.SystemDepartmentDO;
import indi.simple.pms.entity.datatransferobject.SystemDepartmentSearchDTO;

import java.util.List;

/**
 * 系统部门表 system_department 服务类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:10
 */
public interface SystemDepartmentService extends IService<SystemDepartmentDO> {
    boolean add(SystemDepartmentDO systemDepartmentDO);

    boolean delete(List<Long> idList);

    boolean update(SystemDepartmentDO systemDepartmentDO);

    List<SystemDepartmentDO> searchTree(SystemDepartmentSearchDTO systemDepartmentSearchDTO);

    List<Long> getChildrenIdListById(Long id);

    List<Long> getChildrenIdListRecursionByIdList(List<Long> idList);

    Long getParentIdById(Long id);
}

package indi.simple.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import indi.simple.pms.entity.dataobject.SystemMenuDO;
import indi.simple.pms.entity.datatransferobject.SystemMenuSearchDTO;
import indi.simple.pms.entity.viewobject.SystemNavigationMenuVO;

import java.util.List;

/**
 * 系统菜单表 system_menu 服务类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:20
 */
public interface SystemMenuService extends IService<SystemMenuDO> {
    boolean add(SystemMenuDO systemMenuDO);

    boolean delete(List<Long> idList);

    boolean update(SystemMenuDO systemMenuDO);

    List<SystemMenuDO> searchTree(SystemMenuSearchDTO systemMenuSearchDTO);

    List<SystemNavigationMenuVO> getNavigation();

    List<Long> getChildrenIdListById(Long id);

    List<Long> getChildrenIdListRecursionByIdList(List<Long> idList);

    Long getParentIdById(Long id);
}

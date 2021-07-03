package indi.simple.pms.controller;

import indi.simple.pms.aop.log.Log;
import indi.simple.pms.entity.dataobject.SystemMenuDO;
import indi.simple.pms.entity.datatransferobject.SystemMenuSearchDTO;
import indi.simple.pms.entity.viewobject.SystemNavigationMenuVO;
import indi.simple.pms.service.SystemMenuService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 系统菜单表 system_menu 控制类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:22
 */
@RestController
@RequestMapping({"/system/menu"})
@Validated
@AllArgsConstructor
public class SystemMenuController {
    private final SystemMenuService menuService;

    @Log("增加菜单")
    @PostMapping({"/add"})
    @PreAuthorize("hasAuthority('MENU_ADD')")
    public ResponseEntity add(@RequestBody @Validated SystemMenuDO systemMenuDO) {
        boolean flag = this.menuService.add(systemMenuDO);
        return ResponseEntity.ok().body(flag);
    }

    @Log("删除菜单")
    @DeleteMapping({"/delete"})
    @PreAuthorize("hasAuthority('MENU_DELETE')")
    public ResponseEntity delete(@RequestBody @NotNull(message = "不能为null!") List<Long> idList) {
        boolean flag = this.menuService.delete(idList);
        return ResponseEntity.ok().body(flag);
    }

    @Log("修改菜单")
    @PutMapping({"/update"})
    @PreAuthorize("hasAuthority('MENU_UPDATE')")
    public ResponseEntity update(@RequestBody @Validated({SystemMenuDO.Update.class}) SystemMenuDO systemMenuDO) {
        boolean flag = this.menuService.update(systemMenuDO);
        return ResponseEntity.ok().body(flag);
    }

    @GetMapping({"/get/navigation"})
    public ResponseEntity getNavigation() {
        List<SystemNavigationMenuVO> navigationMenuAOList = this.menuService.getNavigation();
        return ResponseEntity.ok().body(navigationMenuAOList);
    }

    @PostMapping({"/search/tree"})
    public ResponseEntity searchTree(@RequestBody SystemMenuSearchDTO systemMenuSearchDTO) {
        List<SystemMenuDO> systemMenuDOList = this.menuService.searchTree(systemMenuSearchDTO);
        return ResponseEntity.ok().body(systemMenuDOList);
    }
}

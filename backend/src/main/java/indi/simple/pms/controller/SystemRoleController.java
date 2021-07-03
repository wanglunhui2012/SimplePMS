package indi.simple.pms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import indi.simple.pms.aop.log.Log;
import indi.simple.pms.entity.dataobject.SystemRoleDO;
import indi.simple.pms.entity.datatransferobject.SystemRoleSearchDTO;
import indi.simple.pms.service.SystemRoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 系统角色表 system_role 控制类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:25
 */
@RestController
@RequestMapping({"/system/role"})
@Validated
@AllArgsConstructor
public class SystemRoleController {
    private final SystemRoleService roleService;

    @Log("增加角色")
    @PostMapping({"/add"})
    @PreAuthorize("hasAuthority('ROLE_ADD')")
    public ResponseEntity add(@RequestBody @Validated SystemRoleDO systemRoleDO) {
        boolean flag = this.roleService.add(systemRoleDO);
        return ResponseEntity.ok().body(flag);
    }

    @Log("删除角色")
    @DeleteMapping({"/delete"})
    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    public ResponseEntity delete(@RequestBody @NotNull(message = "不能为null!") List<Long> idList) {
        boolean flag = this.roleService.delete(idList);
        return ResponseEntity.ok().body(flag);
    }

    @Log("修改角色")
    @PutMapping({"/update"})
    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    public ResponseEntity update(@RequestBody @Validated({SystemRoleDO.Update.class}) SystemRoleDO systemRoleDO) {
        boolean flag = this.roleService.update(systemRoleDO);
        return ResponseEntity.ok().body(flag);
    }

    @PostMapping({"/search/page"})
    public ResponseEntity searchPage(@RequestBody @Validated SystemRoleSearchDTO systemRoleSearchDTO) {
        IPage<SystemRoleDO> systemRoleDOIPage = this.roleService.searchPage(systemRoleSearchDTO);
        return ResponseEntity.ok().body(systemRoleDOIPage);
    }

    @GetMapping("/get/current/roleIdList")
    public ResponseEntity getCurrentRoleIdList(){
        List<Long> roleIdList=this.roleService.getCurrentRoleIdList();

        return ResponseEntity.ok().body(roleIdList);
    }
}

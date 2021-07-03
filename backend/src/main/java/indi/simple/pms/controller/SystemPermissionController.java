package indi.simple.pms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import indi.simple.pms.aop.log.Log;
import indi.simple.pms.entity.businessobject.JwtUserBO;
import indi.simple.pms.entity.dataobject.SystemPermissionDO;
import indi.simple.pms.entity.datatransferobject.SystemPermissionSearchDTO;
import indi.simple.pms.service.SystemPermissionService;
import indi.simple.pms.util.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统权限表 system_permission 控制类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:24
 */
@RestController
@RequestMapping({"/system/permission"})
@Validated
@AllArgsConstructor
public class SystemPermissionController {
    private final SystemPermissionService systemPermissionService;

    @Log("增加权限")
    @PostMapping({"/add"})
    @PreAuthorize("hasAuthority('PERMISSION_ADD')")
    public ResponseEntity add(@RequestBody @Validated SystemPermissionDO systemPermissionDO) {
        boolean flag = this.systemPermissionService.add(systemPermissionDO);
        return ResponseEntity.ok().body(flag);
    }

    @Log("删除权限")
    @DeleteMapping({"/delete"})
    @PreAuthorize("hasAuthority('PERMISSION_DELETE')")
    public ResponseEntity delete(@RequestBody @NotNull(message = "不能为null!") List<Long> idList) {
        boolean flag = this.systemPermissionService.delete(idList);
        return ResponseEntity.ok().body(flag);
    }

    @Log("修改权限")
    @PutMapping({"/update"})
    @PreAuthorize("hasAuthority('PERMISSION_UPDATE')")
    public ResponseEntity update(@RequestBody @Validated({SystemPermissionDO.Update.class}) SystemPermissionDO systemPermissionDO) {
        boolean flag = this.systemPermissionService.update(systemPermissionDO);
        return ResponseEntity.ok().body(flag);
    }

    @GetMapping({"/get/current"})
    public ResponseEntity getCurrentPermission() {
        JwtUserBO jwtUserBO = SecurityUtil.getJwtUserBO();
        return ResponseEntity.ok().body(jwtUserBO.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
    }

    @PostMapping({"/search/page"})
    public ResponseEntity searchPage(@RequestBody SystemPermissionSearchDTO systemPermissionSearchDTO) {
        IPage<SystemPermissionDO> systemPermissionDOIPage = this.systemPermissionService.searchPage(systemPermissionSearchDTO);
        return ResponseEntity.ok().body(systemPermissionDOIPage);
    }
}


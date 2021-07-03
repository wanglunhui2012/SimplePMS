package indi.simple.pms.controller;

import indi.simple.pms.aop.log.Log;
import indi.simple.pms.entity.dataobject.SystemDepartmentDO;
import indi.simple.pms.entity.datatransferobject.SystemDepartmentSearchDTO;
import indi.simple.pms.service.SystemDepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 系统部门表 system_department 控制类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:12
 */
@RestController
@RequestMapping({"/system/department"})
@Validated
@AllArgsConstructor
public class SystemDepartmentController {
    private final SystemDepartmentService systemDepartmentService;

    @Log("增加部门")
    @PostMapping({"/add"})
    @PreAuthorize("hasAuthority('DEPARTMENT_ADD')")
    public ResponseEntity add(@RequestBody @Validated SystemDepartmentDO systemDepartmentDO) {
        boolean flag = this.systemDepartmentService.add(systemDepartmentDO);
        return ResponseEntity.ok().body(flag);
    }

    @Log("删除部门")
    @DeleteMapping({"/delete"})
    @PreAuthorize("hasAuthority('DEPARTMENT_DELETE')")
    public ResponseEntity delete(@RequestBody @NotNull(message = "不能为null!") List<Long> idList) {
        boolean flag = this.systemDepartmentService.delete(idList);
        return ResponseEntity.ok().body(flag);
    }

    @Log("修改部门")
    @PutMapping({"/update"})
    @PreAuthorize("hasAuthority('DEPARTMENT_UPDATE')")
    public ResponseEntity update(@RequestBody @Validated({SystemDepartmentDO.Update.class}) SystemDepartmentDO systemDepartmentDO) {
        boolean flag = this.systemDepartmentService.update(systemDepartmentDO);
        return ResponseEntity.ok().body(flag);
    }

    @PostMapping({"/search/tree"})
    public ResponseEntity searchTree(@RequestBody SystemDepartmentSearchDTO systemDepartmentSearchDTO) {
        List<SystemDepartmentDO> systemDepartmentDOList = this.systemDepartmentService.searchTree(systemDepartmentSearchDTO);
        return ResponseEntity.ok().body(systemDepartmentDOList);
    }
}

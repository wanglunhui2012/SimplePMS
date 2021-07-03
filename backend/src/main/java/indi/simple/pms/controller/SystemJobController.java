package indi.simple.pms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import indi.simple.pms.aop.log.Log;
import indi.simple.pms.entity.dataobject.SystemJobDO;
import indi.simple.pms.entity.datatransferobject.SystemJobSearchDTO;
import indi.simple.pms.service.SystemJobService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 系统职位表 system_job 控制类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:18
 */
@RestController
@RequestMapping({"/system/job"})
@Validated
@AllArgsConstructor
public class SystemJobController {
    private final SystemJobService systemJobService;

    @Log("增加职位")
    @PostMapping({"/add"})
    @PreAuthorize("hasAuthority('JOB_ADD')")
    public ResponseEntity add(@RequestBody @Validated SystemJobDO systemJobDO) {
        boolean flag = this.systemJobService.add(systemJobDO);
        return ResponseEntity.ok().body(flag);
    }

    @Log("删除职位")
    @DeleteMapping({"/delete"})
    @PreAuthorize("hasAuthority('JOB_DELETE')")
    public ResponseEntity delete(@RequestBody @NotNull(message = "不能为null!") List<Long> idList) {
        boolean flag = this.systemJobService.delete(idList);
        return ResponseEntity.ok().body(flag);
    }

    @Log("修改职位")
    @PutMapping({"/update"})
    @PreAuthorize("hasAuthority('JOB_UPDATE')")
    public ResponseEntity update(@RequestBody @Validated({SystemJobDO.Update.class}) SystemJobDO systemRoleDO) {
        boolean flag = this.systemJobService.update(systemRoleDO);
        return ResponseEntity.ok().body(flag);
    }

    @PostMapping({"/search/page"})
    public ResponseEntity searchPage(@RequestBody @Validated SystemJobSearchDTO systemJobSearchDTO) {
        IPage<SystemJobDO> systemJobDOIPage = this.systemJobService.searchPage(systemJobSearchDTO);
        return ResponseEntity.ok().body(systemJobDOIPage);
    }

    @GetMapping("/get/current/jobIdList")
    public ResponseEntity getCurrentJobIdList(){
        List<Long> jobIdList=this.systemJobService.getCurrentJobIdList();

        return ResponseEntity.ok().body(jobIdList);
    }
}

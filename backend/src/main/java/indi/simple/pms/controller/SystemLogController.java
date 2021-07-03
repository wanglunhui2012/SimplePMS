package indi.simple.pms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import indi.simple.pms.auth.PermitAll;
import indi.simple.pms.entity.dataobject.SystemLogDO;
import indi.simple.pms.entity.datatransferobject.SystemLogSearchDTO;
import indi.simple.pms.service.SystemLogService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 系统日志表 system_log 控制类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:20
 */
@RestController
@RequestMapping({"/system/log"})
@Validated
@AllArgsConstructor
public class SystemLogController {
    private final SystemLogService systemLogService;

    @PostMapping({"/search/page"})
    public ResponseEntity searchPage(@RequestBody SystemLogSearchDTO systemLogSearchDTO) {
        IPage<SystemLogDO> systemLogDOIPage = this.systemLogService.searchPage(systemLogSearchDTO);
        return ResponseEntity.ok().body(systemLogDOIPage);
    }

    @PermitAll
    @GetMapping({"/download"})
    public void download(HttpServletResponse response) throws IOException {
        this.systemLogService.download(response);
    }
}

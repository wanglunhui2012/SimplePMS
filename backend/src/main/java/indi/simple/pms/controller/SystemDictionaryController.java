package indi.simple.pms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import indi.simple.pms.aop.log.Log;
import indi.simple.pms.entity.dataobject.SystemDictionaryDO;
import indi.simple.pms.entity.datatransferobject.SystemDictionarySearchDTO;
import indi.simple.pms.service.SystemDictionaryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 系统字典表 system_dictionary 控制类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:14
 */
@RestController
@RequestMapping({"/system/dictionary"})
@Validated
@AllArgsConstructor
public class SystemDictionaryController {
    private final SystemDictionaryService systemDictionaryService;

    @Log("增加字典")
    @PostMapping({"/add"})
    @PreAuthorize("hasAuthority('DICTIONARY_ADD')")
    public ResponseEntity add(@RequestBody @Validated SystemDictionaryDO systemDictionaryDO) {
        boolean flag = this.systemDictionaryService.add(systemDictionaryDO);
        return ResponseEntity.ok().body(flag);
    }

    @Log("删除字典")
    @DeleteMapping({"/delete"})
    @PreAuthorize("hasAuthority('DICTIONARY_DELETE')")
    public ResponseEntity delete(@RequestBody @NotNull(message = "不能为null!") List<Long> idList) {
        boolean flag = this.systemDictionaryService.delete(idList);
        return ResponseEntity.ok().body(flag);
    }

    @Log("修改字典")
    @PutMapping({"/update"})
    @PreAuthorize("hasAuthority('DICTIONARY_UPDATE')")
    public ResponseEntity updateDictionary(@RequestBody @Validated({SystemDictionaryDO.Update.class}) SystemDictionaryDO systemDictionaryDO) {
        boolean flag = this.systemDictionaryService.update(systemDictionaryDO);
        return ResponseEntity.ok().body(flag);
    }

    @PostMapping({"/search/page"})
    public ResponseEntity searchPage(@RequestBody SystemDictionarySearchDTO systemDictionarySearchDTO) {
        IPage<SystemDictionaryDO> systemDictionaryDOIPage = this.systemDictionaryService.searchPage(systemDictionarySearchDTO);
        return ResponseEntity.ok().body(systemDictionaryDOIPage);
    }
}


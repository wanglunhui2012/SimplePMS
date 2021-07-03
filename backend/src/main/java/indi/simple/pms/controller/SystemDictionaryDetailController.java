package indi.simple.pms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import indi.simple.pms.aop.log.Log;
import indi.simple.pms.entity.dataobject.SystemDictionaryDetailDO;
import indi.simple.pms.entity.datatransferobject.SystemDictionaryDetailSearchDTO;
import indi.simple.pms.service.SystemDictionaryDetailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 系统字典详情表 system_dictionary_detail 控制类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:16
 */
@RestController
@RequestMapping({"/system/dictionaryDetail"})
@Validated
@AllArgsConstructor
public class SystemDictionaryDetailController {
    private final SystemDictionaryDetailService systemDictionaryDetailService;

    @Log("增加字典详情")
    @PostMapping({"/add"})
    @PreAuthorize("hasAuthority('DICTIONARY_DETAIL_ADD')")
    public ResponseEntity add(@RequestBody @Validated SystemDictionaryDetailDO systemDictionaryDetailDO) {
        boolean flag = this.systemDictionaryDetailService.add(systemDictionaryDetailDO);
        return ResponseEntity.ok().body(flag);
    }

    @Log("删除字典详情")
    @DeleteMapping({"/delete"})
    @PreAuthorize("hasAuthority('DICTIONARY_DETAIL_DELETE')")
    public ResponseEntity delete(@RequestBody @NotNull(message = "不能为null!") List<Long> idList) {
        boolean flag = this.systemDictionaryDetailService.delete(idList);
        return ResponseEntity.ok().body(flag);
    }

    @Log("修改字典详情")
    @PutMapping({"/update"})
    @PreAuthorize("hasAuthority('DICTIONARY_DETAIL_UPDATE')")
    public ResponseEntity update(@RequestBody @Validated({SystemDictionaryDetailDO.Update.class}) SystemDictionaryDetailDO systemDictionaryDetailDO) {
        boolean flag = this.systemDictionaryDetailService.update(systemDictionaryDetailDO);
        return ResponseEntity.ok().body(flag);
    }

    @PostMapping({"/search/page"})
    public ResponseEntity searchPage(@RequestBody SystemDictionaryDetailSearchDTO systemDictionaryDetailSearchDTO) {
        IPage<SystemDictionaryDetailDO> systemDictionaryDetailDOIPage = this.systemDictionaryDetailService.searchPage(systemDictionaryDetailSearchDTO);
        return ResponseEntity.ok().body(systemDictionaryDetailDOIPage);
    }

    @PostMapping({"/search/byNameList"})
    public ResponseEntity searchByNameList(@RequestBody(required = false) List<String> nameList) {
        Map<String, Map<String,String>> map = this.systemDictionaryDetailService.searchMap(nameList);
        return ResponseEntity.ok().body(map);
    }
}

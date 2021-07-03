package indi.simple.pms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import indi.simple.pms.aop.log.Log;
import indi.simple.pms.entity.dataobject.SystemUserDO;
import indi.simple.pms.entity.datatransferobject.SystemChangePasswordDTO;
import indi.simple.pms.entity.datatransferobject.SystemUserSearchDTO;
import indi.simple.pms.entity.datatransferobject.SystemUserUpdateProfileDTO;
import indi.simple.pms.service.SystemUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 系统用户表 system_user 控制类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:36
 */
@RestController
@RequestMapping({"/system/user"})
@Validated
@AllArgsConstructor
public class SystemUserController {
    private final SystemUserService systemUserService;

    @Log("增加用户")
    @PostMapping({"/add"})
    @PreAuthorize("hasAuthority('USER_ADD')")
    public ResponseEntity add(@RequestParam("json") @NotBlank(message = "不能为null!") String json, @RequestParam(value="avatar",required = false) MultipartFile avatar) {
        this.systemUserService.add(json,avatar);
        return ResponseEntity.ok().body(true);
    }

    @Log("删除用户")
    @DeleteMapping({"/delete"})
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public ResponseEntity delete(@RequestBody @NotNull(message = "不能为null!") List<Long> idList) {
        boolean flag = this.systemUserService.delete(idList);
        return ResponseEntity.ok().body(flag);
    }

    @Log("修改用户")
    @PutMapping({"/update"})
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public ResponseEntity update(@RequestParam("json") @NotBlank(message = "不能为null!") String json, @RequestParam(value="newAvatar",required = false) MultipartFile newAvatar) {
        boolean flag = this.systemUserService.update(json,newAvatar);
        return ResponseEntity.ok().body(flag);
    }

    @PreAuthorize("hasAuthority('USER_GET')")
    @PostMapping({"/search/page"})
    //@SqlSlot("dataScope()")
    public ResponseEntity searchPageUser(@RequestBody SystemUserSearchDTO systemUserSearchDTO) {
        IPage<SystemUserDO> systemUserDOIPage = this.systemUserService.searchPage(systemUserSearchDTO);
        return ResponseEntity.ok().body(systemUserDOIPage);
    }

    @Log("修改密码")
    @PutMapping({"/changePassword"})
    public ResponseEntity changePassword(@RequestBody @Validated SystemChangePasswordDTO changePasswordDTO, HttpServletRequest request) {
        boolean flag = this.systemUserService.changePassword(changePasswordDTO, request);
        return ResponseEntity.ok().body(flag);
    }

    @Log("修改头像")
    @PostMapping({"/upload/avatar"})
    public ResponseEntity uploadAvatar(@RequestParam("userId") @NotNull(message = "不能为null!") Long userId, @RequestParam(value = "file") MultipartFile file) {
        String result = this.systemUserService.uploadAvatar(userId, file);
        return ResponseEntity.ok().body(result);
    }

    @Log("修改Profile")
    @PostMapping("/update/profile")
    public ResponseEntity updateProfile(@RequestBody @Validated SystemUserUpdateProfileDTO systemUserUpdateProfileDTO){
        boolean flag = this.systemUserService.updateProfile(systemUserUpdateProfileDTO);
        return ResponseEntity.ok().body(flag);
    }

    @GetMapping({"/get/current"})
    public ResponseEntity getCurrent() {
        SystemUserDO systemUserDO = this.systemUserService.getCurrent();
        return ResponseEntity.ok().body(systemUserDO);
    }
}

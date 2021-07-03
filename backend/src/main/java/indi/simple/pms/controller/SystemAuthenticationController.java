package indi.simple.pms.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import indi.simple.pms.aop.log.Log;
import indi.simple.pms.auth.PermitAll;
import indi.simple.pms.entity.datatransferobject.SystemLoginDTO;
import indi.simple.pms.entity.viewobject.SystemCaptchaVO;
import indi.simple.pms.entity.viewobject.SystemTokenVO;
import indi.simple.pms.service.SystemAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 系统认证 控制类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:12
 */
@RestController
@RequestMapping("/system/authentication")
@RequiredArgsConstructor
public class SystemAuthenticationController {
    private final SystemAuthenticationService systemAuthenticationService;

    @PermitAll
    @GetMapping({"/captcha"})
    public ResponseEntity getCaptcha() throws IOException {
        String uuid = IdUtil.simpleUUID();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String captcha=systemAuthenticationService.getCaptcha(uuid,baos);

        return ResponseEntity.ok(new SystemCaptchaVO(uuid,"data:image/png;base64," + Base64.encode(baos.toByteArray())));
    }

    @Log(value = "用户登录", isLogin = true)
    @PermitAll
    @PostMapping({"/login"})
    public ResponseEntity login(@RequestBody @Validated SystemLoginDTO systemLoginDTO, HttpServletRequest request) {
        SystemTokenVO systemTokenVO=systemAuthenticationService.login(systemLoginDTO,request);

        return ResponseEntity.ok().body(systemTokenVO);
    }

    @PermitAll // 不需要认证是因为如果恰好在身份信息过期时退出的话会提示身份信息过期，但此时却是已经登录了的
    @DeleteMapping({"/logout"})
    public ResponseEntity logout(HttpServletRequest request) {
        boolean flag=systemAuthenticationService.logout(request);

        return ResponseEntity.ok().body(flag);
    }
}

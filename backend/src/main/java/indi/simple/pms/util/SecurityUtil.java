package indi.simple.pms.util;

import indi.simple.pms.entity.businessobject.JwtUserBO;
import indi.simple.pms.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:39
 * @Description:
 */
public class SecurityUtil {
    public static JwtUserBO getJwtUserBO() {
        try {
            JwtUserBO jwtUserBO = (JwtUserBO) getAuthentication().getPrincipal();
            return jwtUserBO;
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.UNAUTHORIZED, "登录状态过期");
        }
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}


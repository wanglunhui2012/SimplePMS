package indi.simple.pms.auth;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 21:28
 * @Description: 匿名用户入口点
 */
public class NoAuthenticationEntryPoint implements AuthenticationEntryPoint{
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(authException == null ? "Full authentication is required to access this resource" : authException.getMessage());
    }
}
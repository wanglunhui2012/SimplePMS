package indi.simple.pms.auth;

import indi.simple.pms.entity.businessobject.JwtUserBO;
import indi.simple.pms.exception.BadRequestException;
import indi.simple.pms.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 21:27
 * @Description: 一个AuthenticationProvider对应一种认证逻辑，如用户名密码、手机号验证码、微信登录等，
 */
@RequiredArgsConstructor
@Slf4j
public class SystemAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal() == null ? "NONE_PROVIDED" : authentication.getName();
        String password = (String)authentication.getCredentials();
        JwtUserBO jwtUserBO = this.jwtService.getJwtUserBOByUsername(username);
        if (jwtUserBO == null) {
            throw new UsernameNotFoundException("不存在此用户!");
        } else if (!passwordEncoder.matches(password + jwtUserBO.getSalt(), jwtUserBO.getPassword())) {
            throw new BadRequestException("密码错误!");
        } else if (!jwtUserBO.isEnabled()) {
            throw new DisabledException("账号已停用，请联系管理员!");
        } else {
            SystemAuthenticationToken systemAuthenticationToken = new SystemAuthenticationToken(jwtUserBO, jwtUserBO.getPassword(), jwtUserBO.getAuthorities());
            systemAuthenticationToken.setDetails(authentication.getDetails());
            return systemAuthenticationToken;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 是SystemAuthenticationToken或其子类
        return SystemAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

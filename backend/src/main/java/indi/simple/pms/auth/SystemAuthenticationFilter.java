package indi.simple.pms.auth;

import indi.simple.pms.entity.businessobject.JwtUserBO;
import indi.simple.pms.service.JwtService;
import indi.simple.pms.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 21:26
 * @Description:
 */
// 不能位置为Bean，否则该过滤器会执行两次
@Slf4j
@Getter
@Setter
public class SystemAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private String jwtSystemHeader;
    private JwtService jwtService;

    public SystemAuthenticationFilter() {
        super(new AntPathRequestMatcher("/login", "POST"));
    }

    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;
        String requestHeader = request.getHeader(this.jwtSystemHeader);
        String token = requestHeader != null && requestHeader.startsWith("Bearer ") ? requestHeader.substring(7) : "";
        if (StringUtil.isNotEmpty(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
            String username=jwtService.parseToken(token);
            log.debug("开始认证与授权:{}", username);
            JwtUserBO jwtUserBO = this.jwtService.getJwtUserBOByUsername(username);
            SystemAuthenticationToken systemAuthenticationToken = new SystemAuthenticationToken(jwtUserBO, jwtUserBO.getPassword(), jwtUserBO.getAuthorities());
            systemAuthenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));
            log.debug("完成认证与授权用户:{},设置SecurityContextHolder", username);
            SecurityContextHolder.getContext().setAuthentication(systemAuthenticationToken);
        }

        chain.doFilter(request, response);
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        return null;
    }
}

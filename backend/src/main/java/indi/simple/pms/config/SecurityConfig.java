package indi.simple.pms.config;

import indi.simple.pms.auth.NoAuthenticationEntryPoint;
import indi.simple.pms.auth.PermitAll;
import indi.simple.pms.auth.SystemAuthenticationFilter;
import indi.simple.pms.auth.SystemAuthenticationProvider;
import indi.simple.pms.service.JwtService;
import indi.simple.pms.util.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 21:30
 * @Description:
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${jwt.system.header}")
    private String jwtSystemHeader;
    @Autowired
    private JwtService jwtService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // 默认前缀是_ROLE，这里去掉
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        ProviderManager providerManager=new ProviderManager(
                systemAuthenticationProvider()
        );

        return providerManager;
    }

    @Bean
    public NoAuthenticationEntryPoint noAuthenticationEntryPoint(){
        return new NoAuthenticationEntryPoint();
    }

    @Bean
    public SystemAuthenticationProvider systemAuthenticationProvider(){
        return new SystemAuthenticationProvider(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = SpringUtil.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
        Set<String> permitAllUrls = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry:handlerMethodMap.entrySet()){
            RequestMappingInfo requestMappingInfo=entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            PermitAll permitAll = handlerMethod.getMethodAnnotation(PermitAll.class);
            if (permitAll != null) {
                permitAllUrls.addAll(requestMappingInfo.getPatternsCondition().getPatterns());
            }
        }

        httpSecurity
                .exceptionHandling()
                .authenticationEntryPoint(noAuthenticationEntryPoint()) // 匿名用户入口点
                //.accessDeniedHandler(myAccessDeniedHandler) // 全局异常处理如果处理了AccessDeniedException或父类Throwable则这里则会失效
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 禁用session
                .and()
                .authorizeRequests(auth->{
                    auth
                            .antMatchers(HttpMethod.OPTIONS, new String[]{"/**"}).permitAll()
                            .antMatchers(new String[]{"/favicon.ico"}).permitAll()
                            .antMatchers(new String[]{"/MP_verify_oQ8CcJaQqOyucc2m.txt"}).permitAll()
                            .antMatchers(new String[]{"/fileUpload/**"}).permitAll()
                            .antMatchers(new String[]{"/avatarUpload/**"}).permitAll()
                            .antMatchers(new String[]{"/static/**"}).permitAll()
                            .antMatchers(permitAllUrls.toArray(new String[0])).permitAll()
                            .anyRequest().authenticated();
                })
                .csrf().disable() // 禁用csrf
                .headers().frameOptions().disable(); // 禁用frameOptions

        SystemAuthenticationFilter systemAuthenticationFilter=new SystemAuthenticationFilter();
        systemAuthenticationFilter.setJwtSystemHeader(jwtSystemHeader);
        systemAuthenticationFilter.setJwtService(jwtService);
        systemAuthenticationFilter.setAuthenticationManager(this.authenticationManager());
        httpSecurity.addFilterBefore(systemAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}

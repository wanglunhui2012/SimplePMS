package indi.simple.pms.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import indi.simple.pms.auth.SystemAuthenticationToken;
import indi.simple.pms.constant.SystemRedisConstant;
import indi.simple.pms.entity.businessobject.JwtUserBO;
import indi.simple.pms.entity.datatransferobject.SystemLoginDTO;
import indi.simple.pms.entity.viewobject.SystemTokenVO;
import indi.simple.pms.service.JwtService;
import indi.simple.pms.service.SystemAuthenticationService;
import indi.simple.pms.util.CaptchaUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/26 22:57
 * @Description:
 */
@Service
@RequiredArgsConstructor
public class SystemAuthenticationServiceImpl implements SystemAuthenticationService {
    private final CacheProperties cacheProperties;
    private final RedisTemplate redisTemplate;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CaptchaUtil captchaUtil;

    @Override
    @CachePut(cacheNames = SystemRedisConstant.LOGIN_CAPTCHA+"#PT2M",key = "#uuid")
    public String getCaptcha(String uuid, OutputStream os) throws IOException {
        String captcha = captchaUtil.getCaptcha();
        captchaUtil.generateCaptcha(os, captcha);

        return captcha;
    }

    @Override
    @CacheEvict(cacheNames = SystemRedisConstant.LOGIN_CAPTCHA,key = "#systemLoginDTO.uuid")
    public SystemTokenVO login(SystemLoginDTO systemLoginDTO, HttpServletRequest request){
        String captcha = (String)redisTemplate.opsForValue().get(cacheProperties.getRedis().getKeyPrefix()+SystemRedisConstant.LOGIN_CAPTCHA+SystemRedisConstant.SPLIT+systemLoginDTO.getUuid());
        if (StringUtils.isBlank(captcha)) {
            throw new BadCredentialsException("验证码已过期!");
        } else if (systemLoginDTO.getCaptcha().equalsIgnoreCase(captcha)) {
            SystemAuthenticationToken systemAuthenticationToken = new SystemAuthenticationToken(systemLoginDTO.getUsername(), systemLoginDTO.getPassword());
            systemAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            Authentication authentication = authenticationManager.authenticate(systemAuthenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            JwtUserBO jwtUserBO = (JwtUserBO)authentication.getPrincipal();
            String token = jwtService.generateToken(jwtUserBO.getUsername());
            return new SystemTokenVO(token);
        } else {
            throw new BadCredentialsException("验证码错误!");
        }
    }

    @Override
    public boolean logout(HttpServletRequest request) {
        String requestHeader = request.getHeader(jwtService.getJwtSystemHeader());

        if(requestHeader != null && requestHeader.startsWith("Bearer ")){ // 因为使用了@PermitAll，所以需要判断一下，因为未认证也会进入这里
            SecurityContextHolder.clearContext();

            return true;
        }else{
            return false;
        }
    }
}

package indi.simple.pms.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import indi.simple.pms.constant.SystemRedisConstant;
import indi.simple.pms.entity.businessobject.JwtUserBO;
import indi.simple.pms.entity.dataobject.SystemRoleDO;
import indi.simple.pms.entity.dataobject.SystemUserDO;
import indi.simple.pms.exception.BadRequestException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/27 21:10
 * @Description: 对称加密和非对称加密见 https://blog.csdn.net/qq_28114159/article/details/107228467
 */
@Service
@Getter
@Setter
public class JwtService {
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private SystemRoleService systemRoleService;
    @Autowired
    private SystemPermissionService systemPermissionService;
    @Value("${jwt.system.header}")
    private String jwtSystemHeader;
    @Value("${jwt.system.expiration}")
    private Long jwtSystemExpiration;

    private static byte[] sharedSecret = new byte[32];
    static {
        sharedSecret="1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456".getBytes(StandardCharsets.UTF_8); // 256位
        //new SecureRandom().nextBytes(sharedSecret); // 这种方式每次重启sharedSecret都会变，导致重启后之前的token都将验证失效
    }

    public String generateToken(String subject){
        //Date date = new Date();
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                //.issuer("sm")
                .subject(subject)
                //.audience("")
                //.issueTime(date)
                //.expirationTime(new Date(date.getTime()+jwtSystemExpiration))
                //.jwtID("system")
                //.claim("scope", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" ")))
                //.claim("authority","[]")
                .build();
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256).build();
        SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);

        try {
            signedJWT.sign(new MACSigner(sharedSecret));
        }catch (JOSEException e){
            throw new RuntimeException(e);
        }

        String token=signedJWT.serialize();

        return token;
    }

    public String parseToken(String token){
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(sharedSecret);

            boolean flag=signedJWT.verify(verifier);
            if(!flag){
                throw new IllegalStateException("token被修改!");
            }

            return signedJWT.getJWTClaimsSet().getSubject();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Cacheable(cacheNames = SystemRedisConstant.LOGIN_JWTUSER,key = "#username")
    public JwtUserBO getJwtUserBOByUsername(String username){
        SystemUserDO systemUserDO = this.systemUserService.getOne(Wrappers.<SystemUserDO>lambdaQuery().eq(SystemUserDO::getName, username));

        if (systemUserDO == null) {
            throw new BadRequestException("账号不存在!");
        } else {
            JwtUserBO jwtUserBO = new JwtUserBO(systemUserDO);
            jwtUserBO.setDepartmentId(systemUserDO.getDepartmentId());
            List<SystemRoleDO> systemRoleDOList = this.systemRoleService.getByUserId(jwtUserBO.getId());
            jwtUserBO.setRoleIdList(systemRoleDOList.stream().map(SystemRoleDO::getId).collect(Collectors.toList()));
            Collection<? extends GrantedAuthority> permissions = this.systemPermissionService.getPermissionByUserId(systemUserDO.getId()).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
            jwtUserBO.setAuthorities(permissions);
            return jwtUserBO;
        }
    }

    public static void main(String[] args){
        JwtService jwtUtil=new JwtService();
        String token=jwtUtil.generateToken("wanglunhui");
        System.out.println(token);
        System.out.println(jwtUtil.parseToken(token));
    }
}

package indi.simple.pms.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;
import java.util.Collections;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 21:27
 * @Description:
 */
public class SystemAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
    private final Object principal;
    private Object credentials;

    public SystemAuthenticationToken(Object principal, Object credentials) {
        super(Collections.emptyList()); // 默认权限为null
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(false);
    }

    public SystemAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public Object getCredentials() {
        return this.credentials;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
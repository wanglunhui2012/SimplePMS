package indi.simple.pms.entity.businessobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import indi.simple.pms.entity.dataobject.SystemUserDO;
import indi.simple.pms.serializer.GrantedAuthorityDeserializer;
import indi.simple.pms.serializer.GrantedAuthoritySerializer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:14
 * @Description:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class JwtUserBO implements UserDetails {
    private Long id;
    private String name;
    private String password;
    private String salt;
    private String enable;
    private Long departmentId;
    @JsonSerialize(using = GrantedAuthoritySerializer.class)
    @JsonDeserialize(using = GrantedAuthorityDeserializer.class)
    private Collection<? extends GrantedAuthority> authorities;
    private List<Long> roleIdList;

    @Override
    public String getUsername() {
        return this.name;
    }

    public boolean isEnabled() {
        return "1".equals(this.enable);
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public JwtUserBO() {
    }

    public JwtUserBO(SystemUserDO systemUserDO) {
        this.id=systemUserDO.getId();
        this.name=systemUserDO.getName();
        this.password=systemUserDO.getPassword();
        this.salt=systemUserDO.getSalt();
        this.enable=systemUserDO.getEnable();
        this.departmentId=systemUserDO.getDepartmentId();
    }
}

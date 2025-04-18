package com.wzkris.common.security.oauth2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.wzkris.common.security.oauth2.domain.model.AuthApp;
import com.wzkris.common.security.oauth2.domain.model.ClientUser;
import com.wzkris.common.security.oauth2.domain.model.LoginUser;
import com.wzkris.common.security.oauth2.enums.LoginType;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : OAuth2用户信息
 * @create : 2024/10/18 15:54
 * @edit : 2024/12/20 14:30 取代原先设计
 */
@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LoginUser.class),
        @JsonSubTypes.Type(value = ClientUser.class),
        @JsonSubTypes.Type(value = AuthApp.class)
})
public abstract class AuthBaseUser implements OAuth2User, Serializable {

    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final String id;

    private final LoginType loginType;

    private final Set<String> grantedAuthority;

    protected AuthBaseUser(String id, LoginType loginType, Set<String> grantedAuthority) {
        Assert.notNull(id, "id cannot be null");
        Assert.notNull(loginType, "loginType cannot be null");
        this.id = id;
        this.loginType = loginType;
        this.grantedAuthority = grantedAuthority;
    }

    @JsonIgnore
    public abstract String getName();

    @JsonIgnore
    public Map<String, Object> getAttributes() {
        return Collections.emptyMap();
    }

    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(this.grantedAuthority);
    }

}

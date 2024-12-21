package com.wzkris.common.security.oauth2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.wzkris.common.security.oauth2.domain.model.AuthClient;
import com.wzkris.common.security.oauth2.domain.model.ClientUser;
import com.wzkris.common.security.oauth2.domain.model.LoginUser;
import com.wzkris.common.security.oauth2.enums.LoginType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;

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
        @JsonSubTypes.Type(value = AuthClient.class)
})
public abstract class AuthBaseUser implements OAuth2User {

    private final LoginType loginType;

    @Setter
    private Set<String> grantedAuthority;

    protected AuthBaseUser(LoginType loginType, Set<String> grantedAuthority) {
        Assert.notNull(loginType, "登录类型不能为空");
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

    /**
     * 用作反序列化
     */
    @JsonProperty(value = "@class", access = JsonProperty.Access.READ_ONLY)
    public String getClazz() {
        return this.getClass().getName();
    }
}

package com.wzkris.common.security.oauth2.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzkris.common.security.oauth2.enums.UserType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : OAuth2用户信息
 * @date : 2024/10/18 15:54
 */
@Getter
public final class WzUser implements UserDetails, OAuth2User, CredentialsContainer, Serializable {

    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final UserType userType;

    private final String name;

    private final Set<GrantedAuthority> authorities;

    @Setter
    private Object principal;

    private String password;

    public WzUser(UserType userType,
                  String name,
                  Object principal,
                  Collection<? extends GrantedAuthority> authorities) {
        this(userType, name, principal, "", authorities);
    }

    @JsonCreator
    public WzUser(@JsonProperty("userType") UserType userType,
                  @JsonProperty("name") String name,
                  @JsonProperty("principal") Object principal,
                  @JsonProperty("password") String password,
                  @JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities) {
        this.userType = userType;
        this.name = name;
        this.principal = principal;
        this.password = password;
        this.authorities = this.toSet(authorities);
    }

    private Set<GrantedAuthority> toSet(Collection<? extends GrantedAuthority> authorities) {
        Set<GrantedAuthority> set = new HashSet<>(authorities.size());
        set.addAll(authorities);
        return set;
    }

    public Map<String, Object> getAttributes() {
        return Collections.emptyMap();
    }

    @Override
    public String getUsername() {
        return this.getName();
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}

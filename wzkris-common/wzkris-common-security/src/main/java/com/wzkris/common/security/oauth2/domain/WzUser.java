package com.wzkris.common.security.oauth2.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzkris.common.security.oauth2.enums.UserType;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.AuthorityUtils;
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
@FieldNameConstants
public final class WzUser implements UserDetails, OAuth2User, CredentialsContainer, Serializable {

    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final UserType userType;

    private final String name;

    private final Set<String> grantedAuthority;

    private final Object principal;

    private String password;

    public WzUser(UserType userType,
                  String name,
                  Object principal,
                  Collection<String> grantedAuthority) {
        this(userType, name, principal, "", grantedAuthority);
    }

    @JsonCreator
    public WzUser(@JsonProperty(Fields.userType) UserType userType,
                  @JsonProperty(Fields.name) String name,
                  @JsonProperty(Fields.principal) Object principal,
                  @JsonProperty(Fields.password) String password,
                  @JsonProperty(Fields.grantedAuthority) Collection<String> grantedAuthority) {
        this.userType = userType;
        this.name = name;
        this.principal = principal;
        this.password = password;
        this.grantedAuthority = this.toSet(grantedAuthority);
    }

    private Set<String> toSet(Collection<String> grantedAuthority) {
        Set<String> set = new HashSet<>(grantedAuthority.size());
        set.addAll(grantedAuthority);
        return set;
    }

    public Map<String, Object> getAttributes() {
        return Collections.emptyMap();
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(this.grantedAuthority);
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

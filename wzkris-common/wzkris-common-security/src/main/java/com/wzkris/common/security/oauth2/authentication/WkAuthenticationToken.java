package com.wzkris.common.security.oauth2.authentication;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.wzkris.common.security.oauth2.domain.OAuth2User;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

import java.io.Serial;
import java.util.Collection;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 认证信息类
 * @date : 2024/08/07 15:44
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class WkAuthenticationToken extends AbstractAuthenticationToken {

    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final OAuth2User principal;

    @Getter
    private final String clientId;

    public WkAuthenticationToken(OAuth2User principal,
                                 Collection<? extends GrantedAuthority> authorities,
                                 String clientId) {
        super(authorities);
        Assert.notNull(principal, "principal cannot be null");
        Assert.hasText(clientId, "authorizedClientRegistrationId cannot be empty");
        this.principal = principal;
        this.clientId = clientId;
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public OAuth2User getPrincipal() {
        return this.principal;
    }

}

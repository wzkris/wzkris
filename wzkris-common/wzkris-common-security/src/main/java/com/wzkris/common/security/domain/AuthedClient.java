package com.wzkris.common.security.domain;

import com.wzkris.auth.rmi.enums.AuthenticatedType;
import com.wzkris.common.core.domain.CorePrincipal;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.util.*;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 授权客户端
 * @date : 2024/6/14 15:30
 */
@Getter
public class AuthedClient extends CorePrincipal implements OAuth2AuthenticatedPrincipal {

    private final String clientId;

    public AuthedClient() {
        this(null, Collections.emptySet());
    }

    public AuthedClient(String clientId, Set<String> scopes) {
        super(clientId, AuthenticatedType.CLIENT.getValue(), scopes);
        this.clientId = clientId;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(new HashMap<>(0));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(getPermissions());
    }

}

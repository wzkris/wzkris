package com.wzkris.common.security.authentication;

import com.wzkris.common.core.model.RoleContext;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collections;

/**
 * 携带 {@link RoleContext} 的自定义认证 Token。
 * <p>
 * 替代将 RoleContext 塞入 {@link #getDetails()} 的做法，
 * 使 details 回归 WebAuthenticationDetails（IP/SessionId）本职。
 *
 * @author wzkris
 */
@Getter
public class RoleContextAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final RoleContext roleContext;

    protected RoleContextAuthenticationToken(Object principal, Object credentials, RoleContext roleContext) {
        super(principal, credentials, roleContext != null ? AuthorityUtils.createAuthorityList(roleContext.getGrantedAuthority()) : Collections.emptyList());
        this.roleContext = roleContext;
    }

    public static RoleContextAuthenticationToken unauthenticated() {
        return new RoleContextAuthenticationToken(null, null, null);
    }

    public static RoleContextAuthenticationToken authenticated(Object principal, Object credentials, RoleContext roleContext) {
        return new RoleContextAuthenticationToken(principal, credentials, roleContext);
    }

}

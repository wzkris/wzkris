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

    /**
     * 已认证构造：经由父类 3 参构造，authenticated 被置为 true。
     */
    private RoleContextAuthenticationToken(Object principal, Object credentials, RoleContext roleContext) {
        super(principal, credentials, roleContext != null ? AuthorityUtils.createAuthorityList(roleContext.getGrantedAuthority()) : Collections.emptyList());
        this.roleContext = roleContext;
    }

    /**
     * 未认证构造：必须走父类 2 参构造以置 authenticated=false。
     * <p>
     * 父类 {@link UsernamePasswordAuthenticationToken} 的 3 参构造会调用 setAuthenticated(true)，
     * 仅 2 参构造才会置 false。原先 unauthenticated() 复用 3 参构造，导致「未认证」token 实际
     * authenticated=true，配合 null principal 使 isLogin() 误判为已登录、getLoginUser() 返回 null，
     * 调用方取值即触发 NPE。
     */
    private RoleContextAuthenticationToken() {
        super(null, null);
        this.roleContext = null;
    }

    public static RoleContextAuthenticationToken unauthenticated() {
        return new RoleContextAuthenticationToken();
    }

    public static RoleContextAuthenticationToken authenticated(Object principal, Object credentials, RoleContext roleContext) {
        return new RoleContextAuthenticationToken(principal, credentials, roleContext);
    }

}

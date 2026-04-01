package com.wzkris.auth.security.core.refresh;

import com.wzkris.common.core.enums.AuthTypeEnum;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Transient;

import java.util.Collections;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 刷新验证token（未认证状态，用于接收请求参数）
 */
@Getter
@Transient
public final class RefreshAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthTypeEnum authType;

    private final String refreshToken;

    private RefreshAuthenticationToken(AuthTypeEnum authType, String refreshToken) {
        super(Collections.emptyList());
        this.authType = authType;
        this.refreshToken = refreshToken;
        super.setAuthenticated(false);
    }

    /**
     * 创建未认证状态的Token（用于接收请求参数）
     */
    public static RefreshAuthenticationToken unauthenticated(AuthTypeEnum authType, String refreshToken) {
        return new RefreshAuthenticationToken(authType, refreshToken);
    }

    @Override
    public Object getCredentials() {
        return refreshToken;
    }

    @Override
    public Object getPrincipal() {
        return refreshToken;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }

}

package com.wzkris.auth.security.core.refresh;

import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.enums.AuthTypeEnum;
import lombok.Getter;
import org.springframework.security.core.Transient;

/**
 * 刷新验证 token（未认证状态，用于接收请求参数）
 */
@Getter
@Transient
public final class RefreshAuthenticationToken extends CommonAuthenticationToken {

    private final String refreshToken;

    private RefreshAuthenticationToken(AuthTypeEnum authType, String refreshToken) {
        super(authType);
        this.refreshToken = refreshToken;
    }

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
    public void eraseCredentials() {
        super.eraseCredentials();
    }

}

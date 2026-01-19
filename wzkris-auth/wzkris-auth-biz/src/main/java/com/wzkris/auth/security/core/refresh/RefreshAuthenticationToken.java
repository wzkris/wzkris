package com.wzkris.auth.security.core.refresh;

import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.UserPrincipal;
import lombok.Getter;
import org.springframework.security.core.Transient;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 刷新验证token
 */
@Getter
@Transient
public final class RefreshAuthenticationToken extends CommonAuthenticationToken {

    private final AuthTypeEnum authType;

    private final String refreshToken;

    private RefreshAuthenticationToken(AuthTypeEnum authType, String refreshToken) {
        this(authType, refreshToken, null);
    }

    private RefreshAuthenticationToken(AuthTypeEnum authType, String refreshToken, UserPrincipal principal) {
        super(principal);
        this.authType = authType;
        this.refreshToken = refreshToken;
    }

    /**
     * 创建未认证状态的Token（用于接收请求参数）
     */
    public static RefreshAuthenticationToken unauthenticated(AuthTypeEnum authType, String refreshToken) {
        return new RefreshAuthenticationToken(authType, refreshToken);
    }

    /**
     * 创建已认证状态的Token（认证成功后使用）
     */
    public static RefreshAuthenticationToken authenticated(AuthTypeEnum authType, String refreshToken, UserPrincipal principal) {
        return new RefreshAuthenticationToken(authType, refreshToken, principal);
    }

    @Override
    public LoginTypeEnum getLoginType() {
        return LoginTypeEnum.REFRESH;
    }

}

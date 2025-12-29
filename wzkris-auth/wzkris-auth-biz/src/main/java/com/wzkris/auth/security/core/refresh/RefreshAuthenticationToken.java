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

    public RefreshAuthenticationToken(AuthTypeEnum authType, String refreshToken) {
        this(authType, refreshToken, null);
    }

    public RefreshAuthenticationToken(AuthTypeEnum authType, String refreshToken, UserPrincipal principal) {
        super(null, principal);
        this.authType = authType;
        this.refreshToken = refreshToken;
    }

    @Override
    public LoginTypeEnum getLoginType() {
        return LoginTypeEnum.REFRESH;
    }

}

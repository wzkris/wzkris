package com.wzkris.auth.security.core.refresh;

import com.wzkris.auth.security.constants.OAuth2LoginTypeConstant;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.model.MyPrincipal;
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

    private final String refreshToken;

    public RefreshAuthenticationToken(String refreshToken) {
        this(refreshToken, null);
    }

    public RefreshAuthenticationToken(String refreshToken, MyPrincipal principal) {
        super(null, principal);
        this.refreshToken = refreshToken;
    }

    @Override
    public Object getCredentials() {
        return this.refreshToken;
    }

    @Override
    public String getLoginType() {
        return OAuth2LoginTypeConstant.REFRESH;
    }

}

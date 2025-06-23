package com.wzkris.auth.security.core.password;

import com.wzkris.auth.security.constants.OAuth2LoginTypeConstant;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.domain.CorePrincipal;
import lombok.Getter;
import org.springframework.security.core.Transient;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 密码验证token
 */
@Getter
@Transient
public final class PasswordAuthenticationToken extends CommonAuthenticationToken {

    private final String username;

    private final String password;

    private final String captchaId;

    public PasswordAuthenticationToken(String username,
                                       String password,
                                       String captchaId) {
        super(null);
        this.username = username;
        this.password = password;
        this.captchaId = captchaId;
    }

    public PasswordAuthenticationToken(String username, CorePrincipal principal) {
        super(null, principal);
        this.username = username;
        this.password = null;
        this.captchaId = null;
    }

    @Override
    public Object getCredentials() {
        return this.password;
    }

    @Override
    public String getLoginType() {
        return OAuth2LoginTypeConstant.PASSWORD;
    }

}

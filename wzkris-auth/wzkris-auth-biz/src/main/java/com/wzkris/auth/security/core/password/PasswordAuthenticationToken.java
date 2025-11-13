package com.wzkris.auth.security.core.password;

import com.wzkris.auth.security.constants.OAuth2LoginTypeConstant;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.MyPrincipal;
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

    private final AuthTypeEnum authType;

    private final String username;

    private final String password;

    private final String captchaId;

    public PasswordAuthenticationToken(
            AuthTypeEnum authType,
            String username,
            String password,
            String captchaId) {
        super(null);
        this.authType = authType;
        this.username = username;
        this.password = password;
        this.captchaId = captchaId;
    }

    public PasswordAuthenticationToken(
            AuthTypeEnum authType,
            String username,
            MyPrincipal principal) {
        super(null, principal);
        this.authType = authType;
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

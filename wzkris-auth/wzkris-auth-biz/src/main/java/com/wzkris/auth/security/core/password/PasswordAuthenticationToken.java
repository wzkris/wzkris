package com.wzkris.auth.security.core.password;

import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.UserPrincipal;
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

    private PasswordAuthenticationToken(
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

    private PasswordAuthenticationToken(
            AuthTypeEnum authType,
            String username,
            UserPrincipal principal) {
        super(principal);
        this.authType = authType;
        this.username = username;
        this.password = null;
        this.captchaId = null;
    }

    /**
     * 创建未认证状态的Token（用于接收请求参数）
     */
    public static PasswordAuthenticationToken unauthenticated(
            AuthTypeEnum authType,
            String username,
            String password,
            String captchaId) {
        return new PasswordAuthenticationToken(authType, username, password, captchaId);
    }

    /**
     * 创建已认证状态的Token（认证成功后使用）
     */
    public static PasswordAuthenticationToken authenticated(
            AuthTypeEnum authType,
            String username,
            UserPrincipal principal) {
        return new PasswordAuthenticationToken(authType, username, principal);
    }

    @Override
    public LoginTypeEnum getLoginType() {
        return LoginTypeEnum.PASSWORD;
    }

}

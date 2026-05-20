package com.wzkris.auth.security.core.password;

import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.common.core.enums.AuthTypeEnum;
import lombok.Getter;
import org.springframework.security.core.Transient;

/**
 * 密码验证 token（未认证状态，用于接收请求参数）
 */
@Getter
@Transient
public final class PasswordAuthenticationToken extends CommonAuthenticationToken {

    private final String username;

    private final String password;

    private PasswordAuthenticationToken(AuthTypeEnum authType, String username, String password) {
        super(authType);
        this.username = username;
        this.password = password;
    }

    public static PasswordAuthenticationToken unauthenticated(
            AuthTypeEnum authType, String username, String password) {
        return new PasswordAuthenticationToken(authType, username, password);
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }

}

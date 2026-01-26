package com.wzkris.auth.security.core.password;

import com.wzkris.common.core.enums.AuthTypeEnum;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Transient;

import java.util.Collections;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 密码验证token（未认证状态，用于接收请求参数）
 */
@Getter
@Transient
public final class PasswordAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthTypeEnum authType;

    private final String username;

    private final String password;

    private final String captchaId;

    private PasswordAuthenticationToken(
            AuthTypeEnum authType,
            String username,
            String password,
            String captchaId) {
        super(Collections.emptyList());
        this.authType = authType;
        this.username = username;
        this.password = password;
        this.captchaId = captchaId;
        super.setAuthenticated(false);
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

    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public Object getPrincipal() {
        return username;
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

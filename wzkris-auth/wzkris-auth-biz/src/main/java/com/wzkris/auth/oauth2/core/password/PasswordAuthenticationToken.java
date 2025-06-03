package com.wzkris.auth.oauth2.core.password;

import com.wzkris.auth.oauth2.core.CommonAuthenticationToken;
import jakarta.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.Transient;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.Assert;

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

    public PasswordAuthenticationToken(
            String username,
            String password,
            String captchaId,
            Authentication clientPrincipal,
            Set<String> scopes,
            @Nullable Map<String, Object> additionalParameters) {
        super(AuthorizationGrantType.PASSWORD, clientPrincipal, scopes, additionalParameters);
        Assert.notNull(username, "username cannot be null");
        Assert.notNull(password, "password cannot be null");
        Assert.notNull(captchaId, "captchaId cannot be null");
        this.username = username;
        this.password = password;
        this.captchaId = captchaId;
    }
}

package com.thingslink.auth.oauth2.authentication.password;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.thingslink.auth.oauth2.authentication.CommonAuthenticationToken;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 密码验证token
 */
@Getter
public class PasswordAuthenticationToken extends CommonAuthenticationToken {
    private final String username;
    private final String password;

    public PasswordAuthenticationToken(String username,
                                       String password,
                                       Authentication clientPrincipal,
                                       Set<String> scopes,
                                       Map<String, Object> additionalParameters) {
        super(AuthorizationGrantType.PASSWORD, clientPrincipal, scopes, additionalParameters);
        Assert.notNull(username, "username cannot be null");
        Assert.notNull(password, "password cannot be null");
        this.username = username;
        this.password = password;
    }
}

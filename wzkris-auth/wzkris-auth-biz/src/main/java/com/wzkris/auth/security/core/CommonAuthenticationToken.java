package com.wzkris.auth.security.core;

import com.wzkris.common.core.enums.AuthTypeEnum;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

/**
 * 未认证请求 Token 基类，承载多端共用的 {@link AuthTypeEnum}。
 */
@Getter
public abstract class CommonAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthTypeEnum authType;

    protected CommonAuthenticationToken(AuthTypeEnum authType) {
        super(Collections.emptyList());
        this.authType = authType;
        setAuthenticated(false);
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use UsernamePasswordAuthenticationToken instead");
        }
        super.setAuthenticated(false);
    }

}

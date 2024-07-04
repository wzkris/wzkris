package com.wzkris.auth.controller.hooks;

import com.wzkris.common.security.oauth2.domain.OAuth2User;
import jakarta.annotation.Nonnull;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : OAuth2登出钩子
 * @date : 2024/7/4 16:15
 */
public abstract class OAuth2LogoutHook {

    /**
     * 钩子方法
     */
    public final void logoutHook(@Nonnull OAuth2User oAuth2User) {
        if (this.check(oAuth2User)) {
            this.doLogoutHook(oAuth2User);
        }
    }

    /**
     * 最终执行的钩子逻辑
     */
    public abstract void doLogoutHook(@Nonnull OAuth2User oAuth2User);

    /**
     * 是否执行钩子方法
     */
    public abstract boolean check(@Nonnull OAuth2User oAuth2User);
}

package com.thingslink.auth.api;

import com.thingslink.auth.service.CaptchaService;
import com.thingslink.common.security.annotation.InnerAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 内部认证接口
 * @date : 2023/8/5 15:35
 */
@InnerAuth
@RestController
@RequiredArgsConstructor
public class RemoteTokenApiImpl implements RemoteTokenApi {
    private final CaptchaService captchaService;
    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    /**
     * 解锁账号
     */
    @Override
    public void unlockAccount(String username) {
        captchaService.unlock(username);
    }

}

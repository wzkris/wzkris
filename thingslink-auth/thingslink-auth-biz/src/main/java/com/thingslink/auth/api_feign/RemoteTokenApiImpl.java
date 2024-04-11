package com.thingslink.auth.api_feign;

import com.thingslink.auth.api.RemoteTokenApi;
import com.thingslink.auth.service.CaptchaService;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.security.annotation.InnerAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.thingslink.common.core.domain.Result.success;

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
    public void unlockAccount(@PathVariable String username) {
        captchaService.unlock(username);
    }

    /**
     * 根据token获取用户信息
     *
     * @param token 可以是access_token/refresh_token/oidc_token 等等
     * @return 用户信息
     */
    @Override
    public Result<Object> findByToken(String token) {
        OAuth2Authorization oAuth2Authorization = oAuth2AuthorizationService.findByToken(token, null);

        Authentication authentication = oAuth2Authorization == null ? null : oAuth2Authorization.getAttribute(Principal.class.getName());
        return authentication == null ? success() : success(authentication.getPrincipal());
    }

    /**
     * 退出登录
     */
    @Override
    public void logoutByToken(String accessToken) {
        OAuth2Authorization authorization = oAuth2AuthorizationService.findByToken(accessToken, null);
        if (authorization != null) {
            oAuth2AuthorizationService.remove(authorization);
        }
    }

}

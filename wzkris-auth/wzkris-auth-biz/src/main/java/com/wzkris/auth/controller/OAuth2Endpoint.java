package com.wzkris.auth.controller;

import cn.hutool.core.util.StrUtil;
import com.wzkris.auth.controller.hooks.OAuth2LogoutHook;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.oauth2.constants.OAuth2Type;
import com.wzkris.common.security.oauth2.domain.OAuth2User;
import com.wzkris.common.security.oauth2.domain.model.LoginClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.List;

@Tag(name = "OAuth2端点")
@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class OAuth2Endpoint {
    private static final String REDIRECT_URL = "redirect_url";
    private final BearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
    private final OAuth2AuthorizationService oAuth2AuthorizationService;
    private final List<OAuth2LogoutHook> oAuth2LogoutHookList;

    @Operation(summary = "token内省")
    @RequestMapping("/check_token")
    public ResponseEntity<OAuth2User> introspect(String token) {
        // 开始校验token
        if (StringUtil.isBlank(token)) {
            return ResponseEntity.ok(null);
        }
        // 查询token
        OAuth2Authorization oAuth2Authorization = oAuth2AuthorizationService.findByToken(token, null);

        if (oAuth2Authorization == null) {
            return ResponseEntity.ok(null);
        }

        // 客户端模式
        if (oAuth2Authorization.getAuthorizationGrantType().equals(AuthorizationGrantType.CLIENT_CREDENTIALS)) {
            LoginClient loginClient = new LoginClient();
            loginClient.setClientId(oAuth2Authorization.getRegisteredClientId());
            loginClient.setClientName(oAuth2Authorization.getPrincipalName());

            OAuth2User oAuth2User = new OAuth2User(
                    OAuth2Type.CLIENT.getValue(),
                    oAuth2Authorization.getPrincipalName(),
                    loginClient,
                    AuthorityUtils.createAuthorityList(oAuth2Authorization.getAuthorizedScopes())
            );
            return ResponseEntity.ok(oAuth2User);
        }

        // 用户信息
        if (oAuth2Authorization.getAttribute(Principal.class.getName()) instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken) {
            OAuth2User oAuth2User = (OAuth2User) oAuth2AuthenticationToken.getPrincipal();

            return ResponseEntity.ok(oAuth2User);
        }

        return ResponseEntity.ok(null);
    }

    @Operation(summary = "当前用户登出")
    @RequestMapping("/logout")
    public ModelAndView logout(ModelAndView modelAndView, HttpServletRequest request) {
        String accessToken = bearerTokenResolver.resolve(request);
        OAuth2Authorization authorization = oAuth2AuthorizationService.findByToken(accessToken, null);
        if (authorization != null) {
            oAuth2AuthorizationService.remove(authorization);
            OAuth2AuthenticationToken authenticationToken = authorization.getAttribute(Principal.class.getName());
            oAuth2LogoutHookList.forEach(hook -> hook.logoutHook((OAuth2User) authenticationToken.getPrincipal()));
        }

        // 获取请求参数中是否包含 回调地址
        String redirectUrl = request.getParameter(REDIRECT_URL);
        if (StrUtil.isNotBlank(redirectUrl)) {
            modelAndView.setView(new RedirectView(redirectUrl));
        }
        else {
            modelAndView.setStatus(HttpStatusCode.valueOf(204));
        }

        return modelAndView;
    }

}
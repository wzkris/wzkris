package com.thingslink.auth.oauth2.endpoint;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.util.StrUtil;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.security.oauth2.model.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Tag(name = "OAuth2端点")
@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class OAuth2Endpoint {
    private static final String REDIRECT_URL = "redirect_url";
    private final BearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
    private final RegisteredClientRepository registeredClientRepository;
    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    private final PasswordEncoder passwordEncoder;


    @Operation(summary = "token内省")
    @RequestMapping("/check_token")
    public ResponseEntity<?> introspect(String token, HttpServletRequest request) {
//        String basicHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        if (StringUtil.isBlank(basicHeader)) {
//            return ResponseEntity.status(403).build();
//        }
//        basicHeader = basicHeader.replaceAll("Basic ", "");
//
//        String[] basicSplit = Base64Decoder.decodeStr(basicHeader).split(":");
//        if (basicSplit.length != 2) {
//            return ResponseEntity.status(403).build();
//        }
//
//        String clientid = basicSplit[0];
//        String clientSecret = basicSplit[1];
//
//        RegisteredClient registeredClient = registeredClientRepository.findByClientId(clientid);
//        if (registeredClient == null) {
//            return ResponseEntity.status(403).build();
//        }
//
//        if (!this.passwordEncoder.matches(clientSecret, registeredClient.getClientSecret())) {
//            return ResponseEntity.status(403).build();
//        }

        if (StringUtil.isBlank(token)) {
            return ResponseEntity.ok(null);
        }

        OAuth2Authorization oAuth2Authorization = oAuth2AuthorizationService.findByToken(token, null);

        if (oAuth2Authorization == null) {
            return ResponseEntity.ok(null);
        }

        if (oAuth2Authorization.getAuthorizationGrantType().equals(AuthorizationGrantType.CLIENT_CREDENTIALS)) {
            // 客户端模式
        }

        if (oAuth2Authorization.getAttribute(LoginUser.class.getName()) == null) {

        }

        return ResponseEntity.ok(oAuth2Authorization.getAttribute(LoginUser.class.getName()));
    }

    @Operation(summary = "当前用户登出")
    @RequestMapping("/logout")
    public ModelAndView logout(ModelAndView modelAndView, HttpServletRequest request) {
        String accessToken = bearerTokenResolver.resolve(request);
        OAuth2Authorization authorization = oAuth2AuthorizationService.findByToken(accessToken, null);
        if (authorization != null) {
            oAuth2AuthorizationService.remove(authorization);
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

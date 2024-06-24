package com.thingslink.auth.oauth2.endpoint;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.util.StrUtil;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.security.oauth2.constants.OAuth2SecurityConstants;
import com.thingslink.common.security.oauth2.constants.OAuth2Type;
import com.thingslink.common.security.oauth2.domain.OAuth2User;
import com.thingslink.common.security.oauth2.domain.model.LoginClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
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

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<OAuth2User> introspect(String token, HttpServletRequest request) {
        // 校验请求是否由内部应用转发
        String basicHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtil.isBlank(basicHeader) || !basicHeader.startsWith("Basic ")) {
            return ResponseEntity.status(401).build();
        }

        basicHeader = basicHeader.replace("Basic ", StringUtil.EMPTY);

        String[] basicSplit = Base64Decoder.decodeStr(basicHeader).split(":");
        if (basicSplit.length != 2) {
            return ResponseEntity.status(401).build();
        }

        String clientid = basicSplit[0];
        String clientSecret = basicSplit[1];

        RegisteredClient innerClient = registeredClientRepository.findByClientId(clientid);
        if (innerClient == null) {
            return ResponseEntity.status(401).build();
        }

        if (!this.passwordEncoder.matches(clientSecret, innerClient.getClientSecret())) {
            return ResponseEntity.status(401).build();
        }

        if (!innerClient.getScopes().contains(OAuth2SecurityConstants.SCOPE_INNER_REQUEST)) {
            return ResponseEntity.status(403).build();
        }

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

            // 用户权限 + 请求客户端权限 + 内部客户端权限(feign转发)
            List<GrantedAuthority> authorities = new ArrayList<>(oAuth2User.getAuthorities());
            authorities.addAll(AuthorityUtils.createAuthorityList(innerClient.getScopes()));
            authorities.addAll(AuthorityUtils.createAuthorityList(oAuth2Authorization.getAuthorizedScopes()));

            oAuth2User.setAuthorities(authorities);
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

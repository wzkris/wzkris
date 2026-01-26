package com.wzkris.auth.httpservice.token;

import com.wzkris.auth.httpservice.token.req.TokenReq;
import com.wzkris.auth.httpservice.token.resp.TokenResponse;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.model.LoginUser;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@Hidden
@RestController
@RequiredArgsConstructor
public class TokenHttpServiceImpl implements TokenHttpService {

    private final TokenService tokenService;

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    private final RegisteredClientRepository registeredClientRepository;

//    private LoginUser validateClient(String token) {
//        OAuth2Authorization oAuth2Authorization = oAuth2AuthorizationService
//                .findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
//
//        if (oAuth2Authorization == null) return null;
//
//        OAuth2AccessToken accessToken = oAuth2Authorization.getAccessToken().getToken();
//
//        if (accessToken.getExpiresAt() == null
//                || accessToken.getExpiresAt().isBefore(Instant.now())) {
//            return null;
//        }
//
//        RegisteredClient registeredClient = registeredClientRepository
//                .findById(oAuth2Authorization.getRegisteredClientId());
//
//        LoginUser loginClient = new LoginUser();
//        loginClient.setUid(Long.valueOf(registeredClient.getId()));
//        loginClient.set(registeredClient.getClientId());
//        return loginClient;
//    }

    private Set<String> buildScopes(Set<String> authorizedScopes) {
        return authorizedScopes.stream().map(sc -> "SCOPE_" + sc).collect(Collectors.toSet());
    }

    @Override
    public TokenResponse introspect(TokenReq tokenReq) {
        LoginUser loginUser;
//        if (StringUtil.equals(AuthTypeEnum.CLIENT.getValue(), tokenReq.getAuthType())) {
//            loginUser = validateClient(tokenReq.getToken());
//        } else {
        loginUser = tokenService.loadByAccessToken(tokenReq.getAuthType(), tokenReq.getToken());
//        }

        if (loginUser == null) {
            return TokenResponse.error(OAuth2ErrorCodes.INVALID_TOKEN, "token check failed");
        }

        Set<String> permissions = tokenService.loadPermissionsByAccessToken(tokenReq.getAuthType(), tokenReq.getToken());

        return TokenResponse.ok(loginUser, permissions);
    }

}

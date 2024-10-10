package com.wzkris.auth.api;

import com.wzkris.auth.api.domain.ReqToken;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.openfeign.annotation.InnerAuth;
import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.common.security.oauth2.constants.OAuth2Type;
import com.wzkris.common.security.oauth2.domain.WzUser;
import com.wzkris.common.security.oauth2.domain.model.LoginClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.wzkris.common.core.domain.Result.fail;
import static com.wzkris.common.core.domain.Result.success;

@InnerAuth
@RestController
@RequiredArgsConstructor
public class RemoteTokenApiImpl implements RemoteTokenApi {
    private static final String TOKEN_REQ_ID_PREFIX = "token:req_id:";

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    @Override
    public Result<String> getTokenReqId(String token) {
        String reqId = String.valueOf(System.currentTimeMillis());
        RedisUtil.setObj(TOKEN_REQ_ID_PREFIX + token, reqId, 60);
        return success(reqId);
    }

    @Override
    public Result<Object> checkToken(ReqToken reqToken) {
        String reqId = RedisUtil.getObj(TOKEN_REQ_ID_PREFIX + reqToken.getToken());
        if (StringUtil.isBlank(reqId) || !reqId.equals(reqToken.getReqId())) {
            return fail("invalid_request_id");
        }
        OAuth2Authorization oAuth2Authorization = oAuth2AuthorizationService.findByToken(reqToken.getToken(), null);
        if (oAuth2Authorization == null) {
            return fail(OAuth2ErrorCodes.INVALID_TOKEN);
        }

        WzUser wzUser;
        AuthorizationGrantType authorizationGrantType = oAuth2Authorization.getAuthorizationGrantType();
        if (authorizationGrantType.equals(AuthorizationGrantType.CLIENT_CREDENTIALS)) {
            LoginClient loginClient = new LoginClient();
            loginClient.setClientId(oAuth2Authorization.getRegisteredClientId());
            loginClient.setClientName(oAuth2Authorization.getPrincipalName());

            wzUser = new WzUser(OAuth2Type.CLIENT, loginClient.getClientName(),
                    loginClient, AuthorityUtils.createAuthorityList(oAuth2Authorization.getAuthorizedScopes()));
        }
        else {
            UsernamePasswordAuthenticationToken authenticationToken = oAuth2Authorization.getAttribute(Principal.class.getName());
            wzUser = (WzUser) authenticationToken.getPrincipal();
        }

        return success(wzUser);
    }
}

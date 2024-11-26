package com.wzkris.auth.api;

import com.wzkris.auth.api.domain.ReqToken;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.openfeign.annotation.InnerAuth;
import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.common.security.oauth2.constants.CustomErrorCodes;
import com.wzkris.common.security.oauth2.domain.WzUser;
import com.wzkris.common.security.oauth2.domain.model.LoginClient;
import com.wzkris.common.security.oauth2.enums.UserType;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RScript;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

import static com.wzkris.common.core.domain.Result.ok;
import static com.wzkris.common.core.domain.Result.resp;

@Hidden
@InnerAuth
@RestController
@RequiredArgsConstructor
public class RemoteTokenApiImpl implements RemoteTokenApi {
    private static final String TOKEN_REQ_ID_PREFIX = "token:req_id:";

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    @Override
    public Result<String> getTokenReqId(String token) {
        String key = TOKEN_REQ_ID_PREFIX + token;
        String reqId = String.valueOf(System.currentTimeMillis());

        String lua = """
                local reqId = redis.call('GET', KEYS[1])
                if not reqId then
                    reqId = ARGV[1]
                end
                redis.call('SETEX', KEYS[1], ARGV[2], reqId)
                return reqId
                """;

        reqId = RedisUtil.getClient().getScript().eval(RScript.Mode.READ_WRITE, lua,
                RScript.ReturnType.VALUE, List.of(key), reqId, 60);
        return ok(reqId);
    }

    @Override
    public Result<WzUser> checkToken(ReqToken reqToken) {
        String reqId = RedisUtil.getObj(TOKEN_REQ_ID_PREFIX + reqToken.getToken());
        if (StringUtil.isBlank(reqId) || !reqId.equals(reqToken.getReqId())) {
            return resp(BizCode.NOT_FOUND, CustomErrorCodes.INVALID_REQUEST_ID);
        }
        OAuth2Authorization oAuth2Authorization = oAuth2AuthorizationService.findByToken(reqToken.getToken(), null);
        if (oAuth2Authorization == null) {
            return resp(BizCode.INVALID_TOKEN, OAuth2ErrorCodes.INVALID_TOKEN);
        }

        UsernamePasswordAuthenticationToken authenticationToken = oAuth2Authorization.getAttribute(Principal.class.getName());
        WzUser wzUser = null;

        if (authenticationToken != null) {
            wzUser = (WzUser) authenticationToken.getPrincipal();
        }
        else {
            // TODO 为空则为其他授权方式登录，现仅兼容客户端模式，可扩展设备码模式
            AuthorizationGrantType authorizationGrantType = oAuth2Authorization.getAuthorizationGrantType();
            if (authorizationGrantType.equals(AuthorizationGrantType.CLIENT_CREDENTIALS)) {
                LoginClient loginClient = new LoginClient();
                loginClient.setClientId(oAuth2Authorization.getRegisteredClientId());
                loginClient.setClientName(oAuth2Authorization.getPrincipalName());

                wzUser = new WzUser(UserType.CLIENT, loginClient.getClientName(),
                        loginClient, oAuth2Authorization.getAuthorizedScopes());
            }
        }
        return ok(wzUser);
    }
}

package com.wzkris.auth.httpclientimpl.token;

import com.wzkris.auth.httpclient.token.LoginUserClient;
import com.wzkris.auth.httpclient.token.req.LoginUserQueryReq;
import com.wzkris.auth.httpclient.token.req.OAuth2TokenQueryReq;
import com.wzkris.auth.httpclient.token.resp.LoginUserResp;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.model.Result;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Set;

@Slf4j
@Hidden
@RestController
@RequiredArgsConstructor
public class LoginUserClientImpl implements LoginUserClient {

    private final TokenService tokenService;

    private final OAuth2AuthorizationService authorizationService;

    /**
     * 查询用户信息
     * <p>
     * 网关在验证JWT后调用此方法，传递uid和sid，查询用户信息和权限。
     * </p>
     *
     * @param loginUserQueryReq 查询请求，包含authType、uid和sid
     * @return 用户信息和权限，如果sid被拉黑或用户不存在则返回错误
     */
    @Override
    public Result<LoginUserResp> queryInfo(LoginUserQueryReq loginUserQueryReq) {
        final Long uid = loginUserQueryReq.getUid();
        final String sid = loginUserQueryReq.getSid();
        final String authType = loginUserQueryReq.getAuthType();

        if (uid == null || authType == null || sid == null) {
            return Result.unauth("Invalid token: missing subject");
        }

        // 检查 sid 是否不在会话中
        if (tokenService.isRevoked(authType, uid, sid)) {
            return Result.unauth("Token has been revoked");
        }

        // 通过 uid 获取用户信息和权限
        BaseLoginUser loginUser = tokenService.loadLoginUserByUid(authType, uid);
        if (loginUser == null) {
            return Result.unauth("Token has been expired");
        }

        Set<String> permissions = tokenService.loadPermissionsByUid(authType, uid);
        return Result.ok(new LoginUserResp(loginUser, permissions));
    }

    /**
     * 通过OAuth2 token查询用户信息
     * <p>
     * 网关在验证OAuth2 token后调用此方法，通过token查询OAuth2Authorization，提取用户信息和权限。
     * </p>
     *
     * @param request 查询请求，包含token字符串
     * @return 用户信息和权限，如果token无效或不存在则返回错误
     */
    @Override
    public Result<LoginUserResp> queryOAuth2(OAuth2TokenQueryReq request) {
        final String token = request.getToken();

        if (token == null || token.isBlank()) {
            return Result.unauth("Invalid token: token is empty");
        }

        // 通过token查询OAuth2Authorization
        OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        if (authorization == null) {
            return Result.unauth("Token not found or expired");
        }

        // 提取权限信息（从authorizedScopes）
        Set<String> permissions = authorization.getAuthorizedScopes() != null
                ? authorization.getAuthorizedScopes()
                : Set.of();

        // 从OAuth2Authorization中提取Principal
        Principal principal = authorization.getAttribute(Principal.class.getName());
        if (principal == null) {
            return Result.unauth("Principal not found in authorization");
        }

        if (principal instanceof BaseLoginUser loginUser) {
            return Result.ok(new LoginUserResp(loginUser, permissions));
        } else {
            // 尝试从Principal中提取信息构造LoginUser
            // 这里可以根据实际需求扩展，比如从UserDetails转换
            log.warn("Unsupported principal type: {}, principalName: {}", principal.getClass().getName(), authorization.getPrincipalName());
            return Result.unauth("Unsupported principal type: " + principal.getClass().getName());
        }
    }

}

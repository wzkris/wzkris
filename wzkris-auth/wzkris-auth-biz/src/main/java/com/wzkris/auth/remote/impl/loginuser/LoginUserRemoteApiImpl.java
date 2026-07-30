package com.wzkris.auth.remote.impl.loginuser;

import com.wzkris.auth.remote.api.loginuser.LoginUserRemoteApi;
import com.wzkris.auth.remote.api.loginuser.request.LoginUserQueryRequest;
import com.wzkris.auth.remote.api.loginuser.request.OAuth2TokenQueryRequest;
import com.wzkris.auth.remote.api.loginuser.response.LoginUserResponse;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.LoginUser;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.model.RoleContext;
import com.wzkris.common.core.model.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginUserRemoteApiImpl implements LoginUserRemoteApi {

    private final TokenService tokenService;

    private final OAuth2AuthorizationService authorizationService;

    @Override
    public Result<LoginUserResponse> queryInfo(LoginUserQueryRequest request) {
        final Long uid = request.getUid();
        final String sid = request.getSid();
        final AuthTypeEnum authType = request.getAuthType();

        // 检查 sid 是否不在会话中
        if (tokenService.isRevoked(authType.getValue(), uid, sid)) {
            return Result.unauth("Token has been revoked");
        }

        // 通过 uid 获取用户信息和权限
        LoginUser loginUser = tokenService.loadLoginUserByUid(authType.getValue(), uid);
        if (loginUser == null) {
            return Result.unauth("Token has been expired");
        }

        RoleContext roleContext = tokenService.loadRoleContextByUid(authType.getValue(), uid);
        return Result.ok(new LoginUserResponse(loginUser, roleContext));
    }

    @Override
    public Result<LoginUserResponse> queryOAuth2(OAuth2TokenQueryRequest request) {
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
        List<String> permissions = authorization.getAuthorizedScopes() != null
                ? new ArrayList<>(authorization.getAuthorizedScopes())
                : new ArrayList<>();

        // 从OAuth2Authorization中提取Principal
        Principal principal = authorization.getAttribute(Principal.class.getName());
        if (principal == null) {
            return Result.unauth("Principal not found in authorization");
        }

        if (principal instanceof LoginUser loginUser) {
            RoleContext roleContext = new RoleContext(
                    List.of(new UserRole(0L, "oauth2_client", null, null, permissions)));
            return Result.ok(new LoginUserResponse(loginUser, roleContext));
        } else {
            // 尝试从Principal中提取信息构造LoginUser
            // 这里可以根据实际需求扩展，比如从UserDetails转换
            log.warn("Unsupported principal type: {}, principalName: {}", principal.getClass().getName(), authorization.getPrincipalName());
            return Result.unauth("Unsupported principal type: " + principal.getClass().getName());
        }
    }

}

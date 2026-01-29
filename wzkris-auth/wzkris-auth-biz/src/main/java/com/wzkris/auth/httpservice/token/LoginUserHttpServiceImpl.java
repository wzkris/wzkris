package com.wzkris.auth.httpservice.token;

import com.wzkris.auth.httpservice.token.req.LoginUserReq;
import com.wzkris.auth.httpservice.token.resp.LoginUserResp;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.model.LoginUser;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@Hidden
@RestController
@RequiredArgsConstructor
public class LoginUserHttpServiceImpl implements LoginUserHttpService {

    private final TokenService tokenService;

    @Override
    public LoginUserResp query(LoginUserReq loginUserReq) {
        final Long uid = loginUserReq.getUid();
        final String authType = loginUserReq.getAuthType();

        if (uid == null || authType == null) {
            return LoginUserResp.error(OAuth2ErrorCodes.INVALID_TOKEN, "Invalid token: missing subject");
        }

        // 2. 通过 uid 获取用户信息和权限
        LoginUser loginUser = tokenService.loadByUid(authType, uid);
        if (loginUser == null) {
            return LoginUserResp.error(OAuth2ErrorCodes.INVALID_TOKEN, "User not found");
        }

        Set<String> permissions = tokenService.loadPermissionsByUid(authType, uid);
        return LoginUserResp.ok(loginUser, permissions);
    }

}

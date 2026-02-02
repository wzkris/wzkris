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

    /**
     * 查询用户信息
     * <p>
     * 网关在验证JWT后调用此方法，传递uid和sid，查询用户信息和权限。
     * </p>
     *
     * @param loginUserReq 查询请求，包含authType、uid和sid
     * @return 用户信息和权限，如果sid被拉黑或用户不存在则返回错误
     */
    @Override
    public LoginUserResp query(LoginUserReq loginUserReq) {
        final Long uid = loginUserReq.getUid();
        final String sid = loginUserReq.getSid();
        final String authType = loginUserReq.getAuthType();

        if (uid == null || authType == null || sid == null) {
            return LoginUserResp.error(OAuth2ErrorCodes.INVALID_TOKEN, "Invalid token: missing subject");
        }

        // 检查 sid 是否不在会话中
        if (tokenService.isRevoked(authType, uid, sid)) {
            return LoginUserResp.error(OAuth2ErrorCodes.INVALID_TOKEN, "Token has been revoked");
        }

        // 通过 uid 获取用户信息和权限
        LoginUser loginUser = tokenService.loadLoginUserByUid(authType, uid);
        if (loginUser == null) {
            return LoginUserResp.error(OAuth2ErrorCodes.INVALID_TOKEN, "Token has been expired");
        }

        Set<String> permissions = tokenService.loadPermissionsByUid(authType, uid);
        return LoginUserResp.ok(loginUser, permissions);
    }

}

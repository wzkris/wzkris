package com.wzkris.auth.remote.controller.loginuser;

import com.wzkris.auth.remote.api.loginuser.LoginUserRemoteApi;
import com.wzkris.auth.remote.api.loginuser.request.LoginUserQueryRequest;
import com.wzkris.auth.remote.api.loginuser.request.OAuth2TokenQueryRequest;
import com.wzkris.auth.remote.api.loginuser.response.LoginUserResponse;
import com.wzkris.common.core.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "登录用户")
@Slf4j
@RestController
@RequestMapping("/login-user-remote")
@RequiredArgsConstructor
public class LoginUserRemoteController {

    private final LoginUserRemoteApi loginUserRemoteApi;

    /**
     * 查询用户信息
     * <p>
     * 网关在验证JWT后调用此方法，传递uid和sid，查询用户信息和权限。
     * </p>
     *
     * @param request 查询请求，包含authType、uid和sid
     * @return 用户信息和权限，如果sid被拉黑或用户不存在则返回错误
     */
    @Operation(summary = "查询登录用户信息")
    @PostMapping("/query-info")
    public Result<LoginUserResponse> queryInfo(@RequestBody @Valid LoginUserQueryRequest request) {
        return loginUserRemoteApi.queryInfo(request);
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
    @Operation(summary = "通过OAuth2令牌查询登录用户信息")
    @PostMapping("/query-oauth2")
    public Result<LoginUserResponse> queryOAuth2(@RequestBody @Valid OAuth2TokenQueryRequest request) {
        return loginUserRemoteApi.queryOAuth2(request);
    }

}

package com.wzkris.usercenter.remote.controller.tenantuser;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.model.UserRole;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.tenantuser.TenantUserRemoteApi;
import com.wzkris.usercenter.remote.api.tenantuser.request.*;
import com.wzkris.usercenter.remote.api.tenantuser.response.TenantUserQueryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "租户用户")
@RestController
@RequestMapping("/tenant-user-remote")
@RequiredArgsConstructor
public class TenantUserRemoteController {

    private final TenantUserRemoteApi tenantUserRemoteApi;

    @Operation(summary = "查询租户用户列表")
    @PostMapping("/query-list")
    public Result<List<TenantUserQueryResponse>> queryList(@RequestBody @Valid TenantUserQueryRequest request) {
        return tenantUserRemoteApi.queryList(request);
    }

    @Operation(summary = "查询租户管理员")
    @PostMapping("/query-tenant-administrator")
    public Result<TenantUserQueryResponse> queryTenantAdministrator(@RequestBody @Valid TenantIdRequest request) {
        return tenantUserRemoteApi.queryTenantAdministrator(request);
    }

    @Operation(summary = "根据社交code查询租户用户")
    @PostMapping("/query-by-social")
    public Result<TenantUserQueryResponse> queryBySocial(@RequestBody @Valid SocialQueryRequest request) {
        return tenantUserRemoteApi.queryBySocial(request);
    }

    @Operation(summary = "查询租户用户权限")
    @PostMapping("/query-permission")
    public Result<List<UserRole>> queryPermission(@RequestBody TenantUserPermissionQueryRequest request) {
        return tenantUserRemoteApi.queryPermission(request);
    }

    @Operation(summary = "更新租户用户社交账号绑定")
    @PostMapping("/update-social-info")
    public Result<Void> updateSocialInfo(@RequestBody @Valid TenantUserSocialUpdateRequest request) {
        return tenantUserRemoteApi.updateSocialInfo(request);
    }

    @Operation(summary = "更新租户用户登录信息")
    @PostMapping("/update-logininfo")
    public Result<Void> updateLoginInfo(@RequestBody LoginInfoUpdateRequest request) {
        return tenantUserRemoteApi.updateLoginInfo(request);
    }

}

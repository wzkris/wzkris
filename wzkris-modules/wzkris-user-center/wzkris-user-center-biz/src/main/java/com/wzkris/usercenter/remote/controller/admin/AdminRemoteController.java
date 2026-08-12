package com.wzkris.usercenter.remote.controller.admin;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.model.UserRole;
import com.wzkris.usercenter.remote.api.admin.AdminRemoteApi;
import com.wzkris.usercenter.remote.api.admin.request.AdminPermissionQueryRequest;
import com.wzkris.usercenter.remote.api.admin.request.AdminQueryRequest;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.admin.response.AdminListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "管理员")
@RestController
@RequestMapping("/admin-remote")
@RequiredArgsConstructor
public class AdminRemoteController {

    private final AdminRemoteApi adminRemoteApi;

    @Operation(summary = "查询管理员列表")
    @PostMapping("/query-list")
    public Result<List<AdminListResponse>> queryList(@RequestBody @Valid AdminQueryRequest request) {
        return adminRemoteApi.queryList(request);
    }

    @Operation(summary = "查询管理员权限")
    @PostMapping("/query-permission")
    public Result<List<UserRole>> queryPermission(@RequestBody AdminPermissionQueryRequest request) {
        return adminRemoteApi.queryPermission(request);
    }

    @Operation(summary = "更新管理员登录信息")
    @PostMapping("/update-logininfo")
    public Result<Void> updateLoginInfo(@RequestBody LoginInfoUpdateRequest request) {
        return adminRemoteApi.updateLoginInfo(request);
    }

}

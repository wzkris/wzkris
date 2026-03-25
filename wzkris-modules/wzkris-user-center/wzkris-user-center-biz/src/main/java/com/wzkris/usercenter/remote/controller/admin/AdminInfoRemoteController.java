package com.wzkris.usercenter.remote.controller.admin;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.admin.AdminInfoRemoteApi;
import com.wzkris.usercenter.remote.api.admin.request.AdminPermsQueryRequest;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.admin.response.AdminInfoResponse;
import com.wzkris.usercenter.response.permission.AdminPermissionResponse;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/admin-info-remote")
@RequiredArgsConstructor
public class AdminInfoRemoteController {

    private final AdminInfoRemoteApi adminInfoRemoteApi;

    @PostMapping("/query-by-username")
    public Result<AdminInfoResponse> queryByUsername(@RequestBody String username) {
        return adminInfoRemoteApi.queryByUsername(username);
    }

    @PostMapping("/query-by-phonenumber")
    public Result<AdminInfoResponse> queryByPhoneNumber(@RequestBody String phoneNumber) {
        return adminInfoRemoteApi.queryByPhoneNumber(phoneNumber);
    }

    @PostMapping("/query-permission")
    public Result<AdminPermissionResponse> queryPermission(@RequestBody AdminPermsQueryRequest request) {
        return adminInfoRemoteApi.queryPermission(request);
    }

    @PostMapping("/update-logininfo")
    public Result<Void> updateLoginInfo(@RequestBody LoginInfoUpdateRequest loginInfoUpdateRequest) {
        return adminInfoRemoteApi.updateLoginInfo(loginInfoUpdateRequest);
    }

}






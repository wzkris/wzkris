package com.wzkris.usercenter.remote.api.admin;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.admin.request.AdminPermsQueryRequest;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.admin.response.AdminInfoResponse;
import com.wzkris.usercenter.response.permission.AdminPermissionResponse;

public interface AdminInfoRemoteApi {

    Result<AdminInfoResponse> queryByUsername(String username);

    Result<AdminInfoResponse> queryByPhoneNumber(String phoneNumber);

    Result<AdminPermissionResponse> queryPermission(AdminPermsQueryRequest request);

    Result<Void> updateLoginInfo(LoginInfoUpdateRequest loginInfoUpdateRequest);

}


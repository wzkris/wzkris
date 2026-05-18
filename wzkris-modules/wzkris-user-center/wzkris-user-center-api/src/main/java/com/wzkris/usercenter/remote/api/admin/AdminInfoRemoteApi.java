package com.wzkris.usercenter.remote.api.admin;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.admin.request.AdminPermsQueryRequest;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.admin.response.AdminInfoResponse;
import com.wzkris.usercenter.remote.api.admin.response.AdminPermissionResponse;
import com.wzkris.usercenter.request.common.StringValueRequest;

public interface AdminInfoRemoteApi {

    Result<AdminInfoResponse> queryByUsername(StringValueRequest request);

    Result<AdminInfoResponse> queryByPhoneNumber(StringValueRequest request);

    Result<AdminPermissionResponse> queryPermission(AdminPermsQueryRequest request);

    Result<Void> updateLoginInfo(LoginInfoUpdateRequest request);

}


package com.wzkris.usercenter.remote.api.admin;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.admin.request.AdminQueryRequest;
import com.wzkris.usercenter.remote.api.admin.request.AdminPermsQueryRequest;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.admin.response.AdminInfoResponse;
import com.wzkris.usercenter.remote.api.admin.response.AdminPermissionResponse;

import java.util.List;

public interface AdminInfoRemoteApi {

    Result<List<AdminInfoResponse>> queryList(AdminQueryRequest request);

    Result<AdminPermissionResponse> queryPermission(AdminPermsQueryRequest request);

    Result<Void> updateLoginInfo(LoginInfoUpdateRequest request);

}


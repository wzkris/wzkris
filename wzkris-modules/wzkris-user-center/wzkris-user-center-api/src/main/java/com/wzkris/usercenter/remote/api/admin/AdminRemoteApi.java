package com.wzkris.usercenter.remote.api.admin;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.model.UserRole;
import com.wzkris.usercenter.remote.api.admin.request.AdminPermissionQueryRequest;
import com.wzkris.usercenter.remote.api.admin.request.AdminQueryRequest;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.admin.response.AdminListResponse;

import java.util.List;

public interface AdminRemoteApi {

    Result<List<AdminListResponse>> queryList(AdminQueryRequest request);

    Result<List<UserRole>> queryPermission(AdminPermissionQueryRequest request);

    Result<Void> updateLoginInfo(LoginInfoUpdateRequest request);

}


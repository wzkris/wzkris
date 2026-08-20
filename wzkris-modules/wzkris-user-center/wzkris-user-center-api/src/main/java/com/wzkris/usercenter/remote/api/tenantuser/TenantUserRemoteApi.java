package com.wzkris.usercenter.remote.api.tenantuser;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.model.UserRole;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.tenantuser.request.TenantUserPermissionQueryRequest;
import com.wzkris.usercenter.remote.api.tenantuser.request.TenantUserQueryRequest;
import com.wzkris.usercenter.remote.api.tenantuser.request.TenantUserSocialUpdateRequest;
import com.wzkris.usercenter.remote.api.tenantuser.request.SocialQueryRequest;
import com.wzkris.usercenter.remote.api.tenantuser.request.TenantIdRequest;
import com.wzkris.usercenter.remote.api.tenantuser.response.TenantUserQueryResponse;

import java.util.List;

public interface TenantUserRemoteApi {

    Result<List<TenantUserQueryResponse>> queryList(TenantUserQueryRequest request);

    Result<TenantUserQueryResponse> queryTenantAdministrator(TenantIdRequest request);

    Result<TenantUserQueryResponse> queryBySocial(SocialQueryRequest request);

    Result<Void> updateSocialInfo(TenantUserSocialUpdateRequest request);

    Result<List<UserRole>> queryPermission(TenantUserPermissionQueryRequest request);

    Result<Void> updateLoginInfo(LoginInfoUpdateRequest request);

}

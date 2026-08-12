package com.wzkris.usercenter.api.tenantuser;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.api.tenantuser.request.*;
import com.wzkris.usercenter.api.tenantuser.response.TenantUserMngQueryResponse;
import com.wzkris.usercenter.api.tenantuser.response.TenantUserMngPageResponse;
import com.wzkris.usercenter.request.PwdResetRequest;
import com.wzkris.usercenter.response.CheckedSelectResponse;

public interface TenantUserMngApi {

    Result<Page<TenantUserMngPageResponse>> queryPage(TenantUserMngPageRequest request);

    Result<TenantUserMngQueryResponse> queryById(IdRequest request);

    Result<CheckedSelectResponse> queryRoleSelect(TenantUserMngRoleSelectRequest request);

    Result<Void> save(TenantUserMngSaveRequest tenantUserReq);

    Result<Void> update(TenantUserMngUpdateRequest tenantUserReq);

    Result<Void> resetPwd(PwdResetRequest request);

    Result<Void> grantRoles(TenantUserMngGrantRoleRequest request);

    Result<Void> remove(IdListRequest request);

}

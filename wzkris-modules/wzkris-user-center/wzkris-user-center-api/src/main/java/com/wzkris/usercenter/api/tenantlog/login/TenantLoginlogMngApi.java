package com.wzkris.usercenter.api.tenantlog.login;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.tenantlog.login.request.TenantLoginLogMngPageRequest;
import com.wzkris.usercenter.api.tenantlog.login.response.TenantLoginLogMngResponse;

public interface TenantLoginlogMngApi {

    Result<Page<TenantLoginLogMngResponse>> queryPage(TenantLoginLogMngPageRequest request);

}

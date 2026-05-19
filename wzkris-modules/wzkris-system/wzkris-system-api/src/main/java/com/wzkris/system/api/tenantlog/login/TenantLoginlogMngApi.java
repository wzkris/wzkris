package com.wzkris.system.api.tenantlog.login;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.api.tenantlog.request.TenantLoginLogMngPageRequest;
import com.wzkris.system.api.tenantlog.response.TenantLoginLogMngResponse;

public interface TenantLoginlogMngApi {

    Result<Page<TenantLoginLogMngResponse>> queryPage(TenantLoginLogMngPageRequest request);

}

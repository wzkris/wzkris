package com.wzkris.system.api.tenantlog.login;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.request.tenantlog.TenantLoginLogQueryRequest;
import com.wzkris.system.response.tenantlog.TenantLoginLogResponse;

public interface TenantLoginlogMngApi {

    Result<Page<TenantLoginLogResponse>> queryPage(TenantLoginLogQueryRequest request);

}

package com.wzkris.system.api.tenantlog.login;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.request.tenantlog.TenantLoginLogInfoQueryRequest;
import com.wzkris.system.response.tenantlog.TenantLoginLogInfoResponse;

public interface TenantLoginlogInfoApi {

    Result<Page<TenantLoginLogInfoResponse>> queryPage(TenantLoginLogInfoQueryRequest request);

}

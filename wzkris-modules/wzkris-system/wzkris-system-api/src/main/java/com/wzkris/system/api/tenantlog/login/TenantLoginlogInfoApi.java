package com.wzkris.system.api.tenantlog.login;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.api.tenantlog.login.request.TenantLoginLogInfoPageRequest;
import com.wzkris.system.api.tenantlog.login.response.TenantLoginLogInfoResponse;

public interface TenantLoginlogInfoApi {

    Result<Page<TenantLoginLogInfoResponse>> queryPage(TenantLoginLogInfoPageRequest request);

}

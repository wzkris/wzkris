package com.wzkris.usercenter.api.tenantpackage;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.api.tenantpackage.response.TenantPackageInfoResponse;

public interface TenantPackageInfoApi {

    Result<TenantPackageInfoResponse> queryInfo();

}

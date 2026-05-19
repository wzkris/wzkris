package com.wzkris.system.api.tenantlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.api.tenantlog.request.TenantOperateLogInfoPageRequest;
import com.wzkris.system.api.tenantlog.response.TenantOperateLogInfoResponse;

public interface TenantOperateLogInfoApi {

    Result<Page<TenantOperateLogInfoResponse>> queryPage(TenantOperateLogInfoPageRequest request);

}

package com.wzkris.system.api.tenantlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.request.tenantlog.TenantOperateLogQueryRequest;
import com.wzkris.system.response.tenantlog.TenantOperateLogInfoResponse;

public interface TenantOperateLogMngApi {

    Result<Page<TenantOperateLogInfoResponse>> page(TenantOperateLogQueryRequest request);

}

package com.wzkris.system.api.tenantlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.request.tenantlog.TenantOperateLogMngPageRequest;
import com.wzkris.system.response.tenantlog.TenantOperateLogMngResponse;

public interface TenantOperateLogMngApi {

    Result<Page<TenantOperateLogMngResponse>> queryPage(TenantOperateLogMngPageRequest request);

}

package com.wzkris.usercenter.api.tenantlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.tenantlog.operate.request.TenantOperateLogMngPageRequest;
import com.wzkris.usercenter.api.tenantlog.operate.response.TenantOperateLogMngPageResponse;

public interface TenantOperateLogMngApi {

    Result<Page<TenantOperateLogMngPageResponse>> queryPage(TenantOperateLogMngPageRequest request);

}

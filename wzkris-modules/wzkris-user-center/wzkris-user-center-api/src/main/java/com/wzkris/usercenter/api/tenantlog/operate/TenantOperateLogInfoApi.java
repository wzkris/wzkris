package com.wzkris.usercenter.api.tenantlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.tenantlog.operate.request.TenantOperateLogInfoPageRequest;
import com.wzkris.usercenter.api.tenantlog.operate.response.TenantOperateLogInfoPageResponse;

public interface TenantOperateLogInfoApi {

    Result<Page<TenantOperateLogInfoPageResponse>> queryPage(TenantOperateLogInfoPageRequest request);

}

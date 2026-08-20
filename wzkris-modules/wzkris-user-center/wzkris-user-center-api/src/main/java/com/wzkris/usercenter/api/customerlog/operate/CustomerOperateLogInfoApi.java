package com.wzkris.usercenter.api.customerlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.customerlog.operate.request.CustomerOperateLogInfoPageRequest;
import com.wzkris.usercenter.api.customerlog.operate.response.CustomerOperateLogInfoPageResponse;

public interface CustomerOperateLogInfoApi {

    Result<Page<CustomerOperateLogInfoPageResponse>> queryPage(CustomerOperateLogInfoPageRequest request);

}

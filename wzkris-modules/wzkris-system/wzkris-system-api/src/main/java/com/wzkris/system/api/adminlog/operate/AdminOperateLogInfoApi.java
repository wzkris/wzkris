package com.wzkris.system.api.adminlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.request.adminlog.AdminOperateLogInfoQueryRequest;
import com.wzkris.system.response.adminlog.AdminOperateLogInfoResponse;

public interface AdminOperateLogInfoApi {

    Result<Page<AdminOperateLogInfoResponse>> queryPage(AdminOperateLogInfoQueryRequest request);

}

package com.wzkris.system.api.adminlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.request.adminlog.AdminOperateLogQueryRequest;
import com.wzkris.system.response.adminlog.AdminOperateLogResponse;

public interface AdminOperateLogInfoApi {

    Result<Page<AdminOperateLogResponse>> queryPage(AdminOperateLogQueryRequest request);

}

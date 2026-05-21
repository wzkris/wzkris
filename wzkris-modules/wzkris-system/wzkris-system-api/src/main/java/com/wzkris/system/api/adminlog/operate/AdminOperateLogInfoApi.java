package com.wzkris.system.api.adminlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.api.adminlog.operate.request.AdminOperateLogInfoPageRequest;
import com.wzkris.system.api.adminlog.operate.response.AdminOperateLogInfoResponse;

public interface AdminOperateLogInfoApi {

    Result<Page<AdminOperateLogInfoResponse>> queryPage(AdminOperateLogInfoPageRequest request);

}

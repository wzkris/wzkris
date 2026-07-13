package com.wzkris.usercenter.api.adminlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.adminlog.operate.request.AdminOperateLogMngPageRequest;
import com.wzkris.usercenter.api.adminlog.operate.response.AdminOperateLogMngResponse;

public interface AdminOperateLogMngApi {

    Result<Page<AdminOperateLogMngResponse>> queryPage(AdminOperateLogMngPageRequest request);

}

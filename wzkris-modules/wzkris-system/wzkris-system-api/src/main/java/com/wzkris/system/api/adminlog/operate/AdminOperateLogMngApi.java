package com.wzkris.system.api.adminlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.request.adminlog.AdminOperateLogMngPageRequest;
import com.wzkris.system.response.adminlog.AdminOperateLogMngResponse;

public interface AdminOperateLogMngApi {

    Result<Page<AdminOperateLogMngResponse>> queryPage(AdminOperateLogMngPageRequest request);

}

package com.wzkris.usercenter.api.adminlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.adminlog.operate.request.AdminOperateLogInfoPageRequest;
import com.wzkris.usercenter.api.adminlog.operate.response.AdminOperateLogInfoPageResponse;

public interface AdminOperateLogInfoApi {

    Result<Page<AdminOperateLogInfoPageResponse>> queryPage(AdminOperateLogInfoPageRequest request);

}

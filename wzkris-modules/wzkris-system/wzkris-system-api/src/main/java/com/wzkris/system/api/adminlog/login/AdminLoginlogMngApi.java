package com.wzkris.system.api.adminlog.login;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.request.adminlog.AdminLoginLogQueryRequest;
import com.wzkris.system.response.adminlog.AdminLoginLogResponse;

public interface AdminLoginlogMngApi {

    Result<Page<AdminLoginLogResponse>> queryPage(AdminLoginLogQueryRequest request);

}

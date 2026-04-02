package com.wzkris.system.api.adminlog.login;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.request.adminlog.AdminLoginLogMngQueryRequest;
import com.wzkris.system.response.adminlog.AdminLoginLogMngResponse;

public interface AdminLoginlogMngApi {

    Result<Page<AdminLoginLogMngResponse>> queryPage(AdminLoginLogMngQueryRequest request);

}

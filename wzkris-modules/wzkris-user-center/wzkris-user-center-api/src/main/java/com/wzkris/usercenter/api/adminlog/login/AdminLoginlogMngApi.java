package com.wzkris.usercenter.api.adminlog.login;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.adminlog.login.request.AdminLoginLogMngPageRequest;
import com.wzkris.usercenter.api.adminlog.login.response.AdminLoginLogMngResponse;

public interface AdminLoginlogMngApi {

    Result<Page<AdminLoginLogMngResponse>> queryPage(AdminLoginLogMngPageRequest request);

}

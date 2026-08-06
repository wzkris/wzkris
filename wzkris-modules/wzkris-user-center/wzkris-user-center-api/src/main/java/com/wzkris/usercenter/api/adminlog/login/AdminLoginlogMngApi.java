package com.wzkris.usercenter.api.adminlog.login;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.adminlog.login.request.AdminLoginLogMngPageRequest;
import com.wzkris.usercenter.api.adminlog.login.response.AdminLoginLogMngPageResponse;

public interface AdminLoginlogMngApi {

    Result<Page<AdminLoginLogMngPageResponse>> queryPage(AdminLoginLogMngPageRequest request);

}

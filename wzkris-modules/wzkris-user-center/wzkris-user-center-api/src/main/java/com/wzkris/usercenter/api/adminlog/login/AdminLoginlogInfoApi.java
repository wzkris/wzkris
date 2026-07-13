package com.wzkris.usercenter.api.adminlog.login;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.adminlog.login.request.AdminLoginLogInfoPageRequest;
import com.wzkris.usercenter.api.adminlog.login.response.AdminLoginLogInfoResponse;

public interface AdminLoginlogInfoApi {

    Result<Page<AdminLoginLogInfoResponse>> queryPage(AdminLoginLogInfoPageRequest request);

}

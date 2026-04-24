package com.wzkris.system.api.adminlog.login;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.request.adminlog.AdminLoginLogInfoPageRequest;
import com.wzkris.system.response.adminlog.AdminLoginLogInfoResponse;

public interface AdminLoginlogInfoApi {

    Result<Page<AdminLoginLogInfoResponse>> queryPage(AdminLoginLogInfoPageRequest request);

}

package com.wzkris.usercenter.api.admin;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.api.admin.request.AdminInfoBasicUpdateRequest;
import com.wzkris.usercenter.api.admin.response.AdminInfoQueryResponse;
import com.wzkris.usercenter.request.PasswordUpdateRequest;
import com.wzkris.usercenter.request.PhoneNumberUpdateRequest;

public interface AdminInfoApi {

    Result<AdminInfoQueryResponse> queryInfo();

    Result<Void> updateBasicInfo(AdminInfoBasicUpdateRequest request);

    Result<Void> updatePhoneNumber(PhoneNumberUpdateRequest request);

    Result<Void> updatePwd(PasswordUpdateRequest request);

}

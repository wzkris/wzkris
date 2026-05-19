package com.wzkris.usercenter.api.admin;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.api.admin.request.AdminInfoBasicUpdateRequest;
import com.wzkris.usercenter.api.admin.response.AdminInfoResponse;
import com.wzkris.usercenter.api.admin.response.ChatPersonResponse;
import com.wzkris.usercenter.request.PasswordUpdateRequest;
import com.wzkris.usercenter.request.PhoneNumberUpdateRequest;

import java.util.List;

public interface AdminInfoApi {

    Result<AdminInfoResponse> queryInfo();

    Result<List<ChatPersonResponse>> queryChatPersonList();

    Result<Void> updateBasicInfo(AdminInfoBasicUpdateRequest request);

    Result<Void> updatePhoneNumber(PhoneNumberUpdateRequest request);

    Result<Void> updatePwd(PasswordUpdateRequest request);

}

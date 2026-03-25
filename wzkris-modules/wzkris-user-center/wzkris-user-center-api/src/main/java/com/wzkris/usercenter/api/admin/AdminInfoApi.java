package com.wzkris.usercenter.api.admin;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.request.PasswordUpdateRequest;
import com.wzkris.usercenter.request.PhoneNumberUpdateRequest;
import com.wzkris.usercenter.request.admin.AdminInfoBasicUpdateRequest;
import com.wzkris.usercenter.response.admin.AdminInfoResponse;
import com.wzkris.usercenter.response.admin.ChatPersonResponse;

import java.util.List;

public interface AdminInfoApi {

    Result<AdminInfoResponse> queryInfo();

    Result<List<ChatPersonResponse>> queryChatPersonList();

    Result<Void> updateBasicInfo(AdminInfoBasicUpdateRequest request);

    Result<Void> updatePhoneNumber(PhoneNumberUpdateRequest request);

    Result<Void> updatePwd(PasswordUpdateRequest request);

}

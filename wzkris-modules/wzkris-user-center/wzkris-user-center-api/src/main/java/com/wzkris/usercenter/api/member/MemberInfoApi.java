package com.wzkris.usercenter.api.member;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.request.common.PasswordUpdateRequest;
import com.wzkris.usercenter.request.common.PhoneNumberUpdateRequest;
import com.wzkris.usercenter.request.member.MemberInfoBasicUpdateRequest;
import com.wzkris.usercenter.response.member.MemberInfoResponse;

public interface MemberInfoApi {

    Result<MemberInfoResponse> queryInfo();

    Result<Void> updateBasicInfo(MemberInfoBasicUpdateRequest request);

    Result<Void> updatePhoneNumber(PhoneNumberUpdateRequest request);

    Result<Void> updatePwd(PasswordUpdateRequest request);

}

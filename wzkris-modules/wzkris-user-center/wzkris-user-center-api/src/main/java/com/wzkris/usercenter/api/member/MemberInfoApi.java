package com.wzkris.usercenter.api.member;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.api.member.request.MemberInfoBasicUpdateRequest;
import com.wzkris.usercenter.api.member.response.MemberInfoQueryResponse;
import com.wzkris.usercenter.request.PasswordUpdateRequest;
import com.wzkris.usercenter.request.PhoneNumberUpdateRequest;

public interface MemberInfoApi {

    Result<MemberInfoQueryResponse> query();

    Result<Void> updateBasicInfo(MemberInfoBasicUpdateRequest request);

    Result<Void> updatePhoneNumber(PhoneNumberUpdateRequest request);

    Result<Void> updatePwd(PasswordUpdateRequest request);

}

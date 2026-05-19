package com.wzkris.usercenter.remote.api.member;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.member.request.MemberPermsQueryRequest;
import com.wzkris.usercenter.remote.api.member.response.MemberInfoResponse;
import com.wzkris.usercenter.remote.api.member.response.MemberPermissionResponse;
import com.wzkris.usercenter.request.StringValueRequest;

public interface MemberInfoRemoteApi {

    Result<MemberInfoResponse> queryByUsername(StringValueRequest request);

    Result<MemberInfoResponse> queryByPhoneNumber(StringValueRequest request);

    Result<MemberInfoResponse> queryByWexcxCode(StringValueRequest request);

    Result<MemberPermissionResponse> queryPermission(MemberPermsQueryRequest request);

    Result<Void> updateLoginInfo(LoginInfoUpdateRequest request);

}


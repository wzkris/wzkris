package com.wzkris.usercenter.remote.api.member;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.member.request.MemberPermsQueryRequest;
import com.wzkris.usercenter.remote.api.member.response.MemberInfoResponse;
import com.wzkris.usercenter.response.permission.MemberPermissionResponse;

public interface MemberInfoRemoteApi {

    Result<MemberInfoResponse> getByUsername(String username);

    Result<MemberInfoResponse> getByPhoneNumber(String phoneNumber);

    Result<MemberInfoResponse> getByWexcxIdentifier(String xcxIdentifier);

    Result<MemberPermissionResponse> getPermission(MemberPermsQueryRequest request);

    Result<Void> updateLoginInfo(LoginInfoUpdateRequest loginInfoUpdateRequest);

}


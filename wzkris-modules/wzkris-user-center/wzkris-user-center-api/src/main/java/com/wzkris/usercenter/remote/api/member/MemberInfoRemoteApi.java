package com.wzkris.usercenter.remote.api.member;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.member.request.MemberPermsQueryRequest;
import com.wzkris.usercenter.remote.api.member.response.MemberInfoResponse;
import com.wzkris.usercenter.response.permission.MemberPermissionResponse;

public interface MemberInfoRemoteApi {

    Result<MemberInfoResponse> queryByUsername(String username);

    Result<MemberInfoResponse> queryByPhoneNumber(String phoneNumber);

    Result<MemberInfoResponse> queryByWexcxCode(String xcxcode);

    Result<MemberPermissionResponse> queryPermission(MemberPermsQueryRequest request);

    Result<Void> updateLoginInfo(LoginInfoUpdateRequest request);

}


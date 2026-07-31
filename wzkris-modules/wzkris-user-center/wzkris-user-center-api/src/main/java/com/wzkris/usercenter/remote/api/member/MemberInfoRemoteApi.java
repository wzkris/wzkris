package com.wzkris.usercenter.remote.api.member;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.member.request.MemberPermsQueryRequest;
import com.wzkris.usercenter.remote.api.member.request.MemberQueryRequest;
import com.wzkris.usercenter.remote.api.member.request.TenantIdRequest;
import com.wzkris.usercenter.remote.api.member.response.MemberInfoResponse;
import com.wzkris.usercenter.remote.api.member.response.MemberPermissionResponse;
import com.wzkris.usercenter.request.StringValueRequest;

import java.util.List;

public interface MemberInfoRemoteApi {

    Result<List<MemberInfoResponse>> queryList(MemberQueryRequest request);

    Result<MemberInfoResponse> queryTenantAdministrator(TenantIdRequest request);

    Result<MemberInfoResponse> queryByWexcxCode(StringValueRequest request);

    Result<MemberPermissionResponse> queryPermission(MemberPermsQueryRequest request);

    Result<Void> updateLoginInfo(LoginInfoUpdateRequest request);

}


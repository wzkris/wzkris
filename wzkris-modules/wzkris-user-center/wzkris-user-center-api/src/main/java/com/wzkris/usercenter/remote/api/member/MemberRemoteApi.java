package com.wzkris.usercenter.remote.api.member;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.model.UserRole;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.member.request.MemberPermsQueryRequest;
import com.wzkris.usercenter.remote.api.member.request.MemberQueryRequest;
import com.wzkris.usercenter.remote.api.member.request.TenantIdRequest;
import com.wzkris.usercenter.remote.api.member.response.MemberListResponse;
import com.wzkris.usercenter.remote.api.member.response.MemberQueryResponse;
import com.wzkris.usercenter.request.StringValueRequest;

import java.util.List;

public interface MemberRemoteApi {

    Result<List<MemberListResponse>> queryList(MemberQueryRequest request);

    Result<MemberQueryResponse> queryTenantAdministrator(TenantIdRequest request);

    Result<MemberQueryResponse> queryByWexcxCode(StringValueRequest request);

    Result<List<UserRole>> queryPermission(MemberPermsQueryRequest request);

    Result<Void> updateLoginInfo(LoginInfoUpdateRequest request);

}

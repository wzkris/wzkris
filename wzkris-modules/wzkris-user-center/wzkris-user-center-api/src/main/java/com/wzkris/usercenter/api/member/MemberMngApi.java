package com.wzkris.usercenter.api.member;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.request.PwdResetRequest;
import com.wzkris.usercenter.request.StatusUpdateRequest;
import com.wzkris.usercenter.request.member.MemberMngGrantPostRequest;
import com.wzkris.usercenter.request.member.MemberMngQueryRequest;
import com.wzkris.usercenter.request.member.MemberMngSaveRequest;
import com.wzkris.usercenter.request.member.MemberMngUpdateRequest;
import com.wzkris.usercenter.response.CheckedSelectResponse;
import com.wzkris.usercenter.response.member.MemberMngResponse;

import java.util.List;

public interface MemberMngApi {

    Result<Page<MemberMngResponse>> queryPage(MemberMngQueryRequest request);

    Result<MemberMngResponse> queryInfo(Long memberId);

    Result<CheckedSelectResponse> queryPostSelect(Long memberId, String postName);

    Result<Void> save(MemberMngSaveRequest memberReq);

    Result<Void> update(MemberMngUpdateRequest memberReq);

    Result<Void> resetPwd(PwdResetRequest request);

    Result<Void> updateStatus(StatusUpdateRequest request);

    Result<Void> grantPosts(MemberMngGrantPostRequest request);

    Result<Void> remove(List<Long> memberIds);

}

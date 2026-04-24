package com.wzkris.usercenter.api.member;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.request.common.IdListRequest;
import com.wzkris.usercenter.request.common.IdRequest;
import com.wzkris.usercenter.request.common.PwdResetRequest;
import com.wzkris.usercenter.request.member.*;
import com.wzkris.usercenter.response.common.CheckedSelectResponse;
import com.wzkris.usercenter.response.member.MemberMngResponse;

public interface MemberMngApi {

    Result<Page<MemberMngResponse>> queryPage(MemberMngPageRequest request);

    Result<MemberMngResponse> queryInfo(IdRequest request);

    Result<CheckedSelectResponse> queryPostSelect(MemberMngPostSelectRequest request);

    Result<Void> save(MemberMngSaveRequest memberReq);

    Result<Void> update(MemberMngUpdateRequest memberReq);

    Result<Void> resetPwd(PwdResetRequest request);

    Result<Void> grantPosts(MemberMngGrantPostRequest request);

    Result<Void> remove(IdListRequest request);

}

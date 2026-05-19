package com.wzkris.usercenter.api.member;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.api.member.request.*;
import com.wzkris.usercenter.api.member.response.MemberMngResponse;
import com.wzkris.usercenter.request.PwdResetRequest;
import com.wzkris.usercenter.response.CheckedSelectResponse;

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

package com.wzkris.usercenter.api.member;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.api.member.request.*;
import com.wzkris.usercenter.api.member.response.MemberMngQueryResponse;
import com.wzkris.usercenter.api.member.response.MemberMngPageResponse;
import com.wzkris.usercenter.request.PwdResetRequest;
import com.wzkris.usercenter.response.CheckedSelectResponse;

public interface MemberMngApi {

    Result<Page<MemberMngPageResponse>> queryPage(MemberMngPageRequest request);

    Result<MemberMngQueryResponse> queryById(IdRequest request);

    Result<CheckedSelectResponse> queryPostSelect(MemberMngPostSelectRequest request);

    Result<Void> save(MemberMngSaveRequest memberReq);

    Result<Void> update(MemberMngUpdateRequest memberReq);

    Result<Void> resetPwd(PwdResetRequest request);

    Result<Void> grantPosts(MemberMngGrantPostRequest request);

    Result<Void> remove(IdListRequest request);

}

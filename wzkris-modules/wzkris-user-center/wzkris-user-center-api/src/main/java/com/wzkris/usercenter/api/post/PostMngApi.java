package com.wzkris.usercenter.api.post;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.request.common.IdListRequest;
import com.wzkris.usercenter.request.common.IdRequest;
import com.wzkris.usercenter.request.post.PostMngPageRequest;
import com.wzkris.usercenter.request.post.PostMngSaveRequest;
import com.wzkris.usercenter.request.post.PostMngUpdateRequest;
import com.wzkris.usercenter.response.common.CheckedSelectTreeResponse;
import com.wzkris.usercenter.response.post.PostInfoResponse;

public interface PostMngApi {

    Result<Page<PostInfoResponse>> queryPage(PostMngPageRequest request);

    Result<PostInfoResponse> queryInfo(IdRequest request);

    Result<CheckedSelectTreeResponse> queryRoleMenuSelectTree(IdRequest request);

    Result<Void> save(PostMngSaveRequest request);

    Result<Void> update(PostMngUpdateRequest request);

    Result<Void> remove(IdListRequest request);

}

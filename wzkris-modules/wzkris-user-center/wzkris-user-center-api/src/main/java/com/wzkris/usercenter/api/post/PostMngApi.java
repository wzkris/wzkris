package com.wzkris.usercenter.api.post;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.api.post.request.PostMngPageRequest;
import com.wzkris.usercenter.api.post.request.PostMngSaveRequest;
import com.wzkris.usercenter.api.post.request.PostMngUpdateRequest;
import com.wzkris.usercenter.api.post.response.PostMngResponse;
import com.wzkris.usercenter.response.CheckedSelectTreeResponse;

public interface PostMngApi {

    Result<Page<PostMngResponse>> queryPage(PostMngPageRequest request);

    Result<PostMngResponse> queryInfo(IdRequest request);

    Result<CheckedSelectTreeResponse> queryRoleMenuSelectTree(IdRequest request);

    Result<Void> save(PostMngSaveRequest request);

    Result<Void> update(PostMngUpdateRequest request);

    Result<Void> remove(IdListRequest request);

}

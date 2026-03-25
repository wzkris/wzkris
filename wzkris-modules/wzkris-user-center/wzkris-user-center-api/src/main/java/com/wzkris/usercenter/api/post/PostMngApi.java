package com.wzkris.usercenter.api.post;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.request.StatusUpdateRequest;
import com.wzkris.usercenter.request.post.PostMngQueryRequest;
import com.wzkris.usercenter.request.post.PostMngSaveRequest;
import com.wzkris.usercenter.request.post.PostMngUpdateRequest;
import com.wzkris.usercenter.response.CheckedSelectTreeResponse;
import com.wzkris.usercenter.response.post.PostInfoResponse;

import java.util.List;

public interface PostMngApi {

    Result<Page<PostInfoResponse>> queryPage(PostMngQueryRequest request);

    Result<PostInfoResponse> queryInfo(Long postId);

    Result<CheckedSelectTreeResponse> queryRoleMenuSelectTree(Long postId);

    Result<Void> save(PostMngSaveRequest request);

    Result<Void> update(PostMngUpdateRequest request);

    Result<Void> updateStatus(StatusUpdateRequest request);

    Result<Void> remove(List<Long> postIds);

}

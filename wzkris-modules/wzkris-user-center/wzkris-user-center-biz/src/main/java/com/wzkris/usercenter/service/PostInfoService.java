package com.wzkris.usercenter.service;

import com.wzkris.common.orm.plus.IServicePlus;
import com.wzkris.usercenter.domain.PostInfoDO;
import com.wzkris.usercenter.response.SelectResponse;
import jakarta.annotation.Nullable;

import java.util.List;

public interface PostInfoService extends IServicePlus<PostInfoDO> {

    List<PostInfoDO> listByMemberId(Long memberId);

    List<SelectResponse> listSelect(@Nullable String postName);

    boolean savePost(PostInfoDO post, List<Long> menuIds);

    boolean updatePost(PostInfoDO post, List<Long> menuIds);

    boolean removePosts(List<Long> postIds);

    boolean existMember(List<Long> postIds);

}

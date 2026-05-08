package com.wzkris.usercenter.impl.post;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.model.TenantLoginUser;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.api.post.PostMngApi;
import com.wzkris.usercenter.domain.PostInfoDO;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.request.post.PostMngPageRequest;
import com.wzkris.usercenter.request.post.PostMngSaveRequest;
import com.wzkris.usercenter.request.post.PostMngUpdateRequest;
import com.wzkris.usercenter.response.common.CheckedSelectTreeResponse;
import com.wzkris.usercenter.response.post.PostMngResponse;
import com.wzkris.usercenter.service.MenuInfoService;
import com.wzkris.usercenter.service.PostInfoService;
import com.wzkris.usercenter.service.TenantInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostMngApiImpl extends AbstractApi implements PostMngApi {

    private final TenantInfoService tenantInfoService;

    private final PostInfoService postInfoService;

    private final MenuInfoService menuInfoService;

    @Override
    public Result<Page<PostMngResponse>> queryPage(PostMngPageRequest request) {
        startPage(request);
        List<PostInfoDO> list = postInfoService.list(this.buildQueryWrapper(request));
        return getPageResult(BeanUtil.convert(list, PostMngResponse.class));
    }

    private LambdaQueryWrapper<PostInfoDO> buildQueryWrapper(PostMngPageRequest request) {
        return new LambdaQueryWrapper<PostInfoDO>()
                .like(StringUtil.isNotEmpty(request.getPostName()), PostInfoDO::getPostName, request.getPostName())
                .eq(request.getStatus() != null, PostInfoDO::getStatus, request.getStatus())
                .orderByDesc(PostInfoDO::getPostSort, PostInfoDO::getPostId);
    }

    @Override
    public Result<PostMngResponse> queryInfo(IdRequest request) {
        return ok(BeanUtil.convert(postInfoService.getById(request.getId()), PostMngResponse.class));
    }

    @Override
    public Result<CheckedSelectTreeResponse> queryRoleMenuSelectTree(IdRequest request) {
        Long postId = request.getId();
        CheckedSelectTreeResponse checkedSelectTreeResponse = new CheckedSelectTreeResponse();
        checkedSelectTreeResponse.setCheckedKeys(menuInfoService.listMenuIdByPostId(postId));
        checkedSelectTreeResponse.setSelectTrees(menuInfoService.listTenantSelectTree(SecurityUtil.getUid()));
        return ok(checkedSelectTreeResponse);
    }

    @Override
    public Result<Void> save(PostMngSaveRequest request) {
        if (!tenantInfoService.checkPostLimit(SecurityUtil.getLoginUser(TenantLoginUser.class).getTenantId())) {
            return requestFail("当前租户职位数量已达到上限");
        }
        PostInfoDO post = BeanUtil.convert(request, PostInfoDO.class);
        post.setStatus(request.getStatus());
        return toRes(postInfoService.savePost(post, request.getMenuIds()));
    }

    @Override
    public Result<Void> update(PostMngUpdateRequest request) {
        PostInfoDO post = BeanUtil.convert(request, PostInfoDO.class);
        post.setStatus(request.getStatus());
        return toRes(postInfoService.updatePost(post, request.getMenuIds()));
    }

    @Override
    public Result<Void> remove(IdListRequest request) {
        List<Long> postIds = request.getIds();
        if (postInfoService.existMember(postIds)) {
            return requestFail("当前职位已被分配");
        }
        return toRes(postInfoService.removePosts(postIds));
    }

}

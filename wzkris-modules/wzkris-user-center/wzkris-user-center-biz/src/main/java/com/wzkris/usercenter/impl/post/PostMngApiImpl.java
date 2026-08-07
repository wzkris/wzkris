package com.wzkris.usercenter.impl.post;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.usercenter.api.post.PostMngApi;
import com.wzkris.usercenter.api.post.request.PostMngPageRequest;
import com.wzkris.usercenter.api.post.request.PostMngSaveRequest;
import com.wzkris.usercenter.api.post.request.PostMngUpdateRequest;
import com.wzkris.usercenter.api.post.response.PostMngQueryResponse;
import com.wzkris.usercenter.api.post.response.PostMngPageResponse;
import com.wzkris.usercenter.domain.PostInfoDO;
import com.wzkris.usercenter.response.CheckedSelectTreeResponse;
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
    public Result<Page<PostMngPageResponse>> queryPage(PostMngPageRequest request) {
        IPage<PostInfoDO> page = postInfoService.page(request.buildPage(), this.buildQueryWrapper(request));
        return ok(Page.of(page, BeanCopierUtil.copyList(page.getRecords(), PostMngPageResponse.class)));
    }

    private LambdaQueryWrapper<PostInfoDO> buildQueryWrapper(PostMngPageRequest request) {
        return new LambdaQueryWrapper<PostInfoDO>()
                .like(StringUtil.isNotEmpty(request.getPostName()), PostInfoDO::getPostName, request.getPostName())
                .eq(request.getStatus() != null, PostInfoDO::getStatus, request.getStatus())
                .orderByDesc(PostInfoDO::getPostSort, PostInfoDO::getId);
    }

    @Override
    public Result<PostMngQueryResponse> queryById(IdRequest request) {
        return ok(BeanCopierUtil.copy(postInfoService.getById(request.getId()), PostMngQueryResponse.class));
    }

    @Override
    public Result<CheckedSelectTreeResponse> queryMenuSelectTree(IdRequest request) {
        Long postId = request.getId();
        CheckedSelectTreeResponse checkedSelectTreeResponse = new CheckedSelectTreeResponse();
        checkedSelectTreeResponse.setCheckedKeys(menuInfoService.listMenuIdByPostId(postId));
        checkedSelectTreeResponse.setSelectTrees(menuInfoService.listTenantSelectTree(SecurityUtil.getUid()));
        return ok(checkedSelectTreeResponse);
    }

    @Override
    public Result<Void> save(PostMngSaveRequest request) {
        if (!tenantInfoService.checkPostLimit(SecurityUtil.getTenantId())) {
            return requestFail("当前租户职位数量已达到上限");
        }
        PostInfoDO post = BeanCopierUtil.copy(request, PostInfoDO.class);
        post.setStatus(request.getStatus());
        return toRes(postInfoService.savePost(post, request.getMenuIds()));
    }

    @Override
    public Result<Void> update(PostMngUpdateRequest request) {
        PostInfoDO post = BeanCopierUtil.copy(request, PostInfoDO.class);
        post.setStatus(request.getStatus());
        return toRes(postInfoService.updatePost(post, request.getMenuIds()));
    }

    @Override
    public Result<Void> remove(IdListRequest request) {
        List<Long> postIds = request.getIdList();
        if (postInfoService.existMember(postIds)) {
            return requestFail("当前职位已被分配");
        }
        return toRes(postInfoService.removePosts(postIds));
    }

}

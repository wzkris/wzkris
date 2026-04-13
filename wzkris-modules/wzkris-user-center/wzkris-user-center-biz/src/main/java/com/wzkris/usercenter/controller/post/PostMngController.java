package com.wzkris.usercenter.controller.post;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckTenantPerms;
import com.wzkris.common.security.enums.CheckMode;
import com.wzkris.usercenter.api.post.PostMngApi;
import com.wzkris.usercenter.request.StatusUpdateRequest;
import com.wzkris.usercenter.request.post.PostMngQueryRequest;
import com.wzkris.usercenter.request.post.PostMngSaveRequest;
import com.wzkris.usercenter.request.post.PostMngUpdateRequest;
import com.wzkris.usercenter.response.CheckedSelectTreeResponse;
import com.wzkris.usercenter.response.post.PostInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "租户职位管理")
@Validated
@RestController
@RequestMapping("/post-manage")
@RequiredArgsConstructor
public class PostMngController {

    private final PostMngApi postMngApi;

    @Operation(summary = "职位分页")
    @GetMapping("/query-page")
    @CheckTenantPerms("user-mod:post-mng:page")
    public Result<Page<PostInfoResponse>> queryPage(PostMngQueryRequest request) {
        return postMngApi.queryPage(request);
    }

    @Operation(summary = "职位详细信息")
    @GetMapping("/query-info/{postId}")
    @CheckTenantPerms("user-mod:post-mng:page")
    public Result<PostInfoResponse> queryInfo(@PathVariable Long postId) {
        return postMngApi.queryInfo(postId);
    }

    @Operation(summary = "职位-菜单选择树")
    @GetMapping({"/query-menu-checked-selecttree/", "/query-menu-checked-selecttree/{postId}"})
    @CheckTenantPerms(
            value = {"user-mod:post-mng:edit", "user-mod:post-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectTreeResponse> queryRoleMenuSelectTree(@PathVariable(required = false) Long postId) {
        return postMngApi.queryRoleMenuSelectTree(postId);
    }

    @Operation(summary = "新增职位")
    @OperateLog(title = "职位管理", subTitle = "新增职位", type = OperateTypeEnum.INSERT)
    @PostMapping("/save")
    @CheckTenantPerms("user-mod:post-mng:add")
    public Result<Void> save(@Validated @RequestBody PostMngSaveRequest request) {
        return postMngApi.save(request);
    }

    @Operation(summary = "修改职位")
    @OperateLog(title = "职位管理", subTitle = "修改职位", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update")
    @CheckTenantPerms("user-mod:post-mng:edit")
    public Result<Void> update(@Validated @RequestBody PostMngUpdateRequest request) {
        return postMngApi.update(request);
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "用户管理", subTitle = "状态修改", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update-status")
    @CheckTenantPerms("user-mod:post-mng:edit")
    public Result<Void> updateStatus(@RequestBody StatusUpdateRequest request) {
        return postMngApi.updateStatus(request);
    }

    @Operation(summary = "删除职位")
    @OperateLog(title = "职位管理", subTitle = "删除职位", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckTenantPerms("user-mod:post-mng:remove")
    public Result<Void> remove(@RequestBody @NotEmpty(message = "{invalidParameter.id.invalid}")
                               List<Long> postIds) {
        return postMngApi.remove(postIds);
    }

}


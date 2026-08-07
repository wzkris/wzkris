package com.wzkris.usercenter.controller.post;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.common.security.enums.CheckMode;
import com.wzkris.usercenter.api.post.PostMngApi;
import com.wzkris.usercenter.api.post.request.PostMngPageRequest;
import com.wzkris.usercenter.api.post.request.PostMngSaveRequest;
import com.wzkris.usercenter.api.post.request.PostMngUpdateRequest;
import com.wzkris.usercenter.api.post.response.PostMngQueryResponse;
import com.wzkris.usercenter.api.post.response.PostMngPageResponse;
import com.wzkris.usercenter.response.CheckedSelectTreeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "租户职位管理")
@Validated
@RestController
@RequestMapping("/post-manage")
@RequiredArgsConstructor
public class PostMngController {

    private static final String PERM_PREFIX = "user-mod:post-mng:";

    private final PostMngApi postMngApi;

    @Operation(summary = "职位分页")
    @GetMapping("/query-page")
    @CheckPerms(checkTypes = AuthTypeEnum.TENANT, prefix = PERM_PREFIX, value = "page")
    public Result<Page<PostMngPageResponse>> queryPage(@ParameterObject PostMngPageRequest request) {
        return postMngApi.queryPage(request);
    }

    @Operation(summary = "职位详细信息")
    @GetMapping("/query-id/{id}")
    @CheckPerms(checkTypes = AuthTypeEnum.TENANT, prefix = PERM_PREFIX, value = "page")
    public Result<PostMngQueryResponse> queryById(@ParameterObject IdRequest request) {
        return postMngApi.queryById(request);
    }

    @Operation(summary = "职位-菜单选择树")
    @GetMapping({"/query-menu-checked-selecttree/", "/query-menu-checked-selecttree/{id}"})
    @CheckPerms(checkTypes = AuthTypeEnum.TENANT, prefix = PERM_PREFIX,
            value = {"edit", "add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectTreeResponse> queryMenuSelectTree(@ParameterObject IdRequest request) {
        return postMngApi.queryMenuSelectTree(request);
    }

    @Operation(summary = "新增职位")
    @OperateLog(title = "职位管理", subTitle = "新增职位", type = OperateTypeEnum.INSERT)
    @PostMapping("/save")
    @CheckPerms(checkTypes = AuthTypeEnum.TENANT, prefix = PERM_PREFIX, value = "add")
    public Result<Void> save(@Validated @RequestBody PostMngSaveRequest request) {
        return postMngApi.save(request);
    }

    @Operation(summary = "修改职位")
    @OperateLog(title = "职位管理", subTitle = "修改职位", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update")
    @CheckPerms(checkTypes = AuthTypeEnum.TENANT, prefix = PERM_PREFIX, value = "edit")
    public Result<Void> update(@Validated @RequestBody PostMngUpdateRequest request) {
        return postMngApi.update(request);
    }

    @Operation(summary = "删除职位")
    @OperateLog(title = "职位管理", subTitle = "删除职位", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckPerms(checkTypes = AuthTypeEnum.TENANT, prefix = PERM_PREFIX, value = "remove")
    public Result<Void> remove(@RequestBody @Valid IdListRequest request) {
        return postMngApi.remove(request);
    }

}


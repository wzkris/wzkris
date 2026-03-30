package com.wzkris.usercenter.controller.member;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckTenantPerms;
import com.wzkris.common.security.enums.CheckMode;
import com.wzkris.usercenter.api.member.MemberMngApi;
import com.wzkris.usercenter.request.PwdResetRequest;
import com.wzkris.usercenter.request.StatusUpdateRequest;
import com.wzkris.usercenter.request.member.MemberMngGrantPostRequest;
import com.wzkris.usercenter.request.member.MemberMngQueryRequest;
import com.wzkris.usercenter.request.member.MemberMngSaveRequest;
import com.wzkris.usercenter.request.member.MemberMngUpdateRequest;
import com.wzkris.usercenter.response.CheckedSelectResponse;
import com.wzkris.usercenter.response.member.MemberMngResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "租户成员管理")
@Validated
@RestController
@RequestMapping("/member-manage")
@RequiredArgsConstructor
public class MemberMngController {

    private final MemberMngApi memberMngApi;

    @Operation(summary = "分页列表")
    @GetMapping("/query-page")
    @CheckTenantPerms("user-mod:member-mng:page")
    public Result<Page<MemberMngResponse>> queryPage(MemberMngQueryRequest request) {
        return memberMngApi.queryPage(request);
    }

    @Operation(summary = "成员详细信息")
    @GetMapping("/query-info/{memberId}")
    @CheckTenantPerms("user-mod:member-mng:page")
    public Result<MemberMngResponse> queryInfo(@PathVariable Long memberId) {
        return memberMngApi.queryInfo(memberId);
    }

    @Operation(summary = "成员-职位选择列表")
    @GetMapping({"/query-post-checked-select/", "/query-post-checked-select/{memberId}"})
    @CheckTenantPerms(
            value = {"user-mod:member-mng:edit", "user-mod:member-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectResponse> queryPostSelect(@PathVariable(required = false) Long memberId, String postName) {
        return memberMngApi.queryPostSelect(memberId, postName);
    }

    @Operation(summary = "新增成员")
    @OperateLog(title = "成员管理", subTitle = "新增成员", type = OperateTypeEnum.INSERT)
    @PostMapping("/save")
    @CheckTenantPerms("user-mod:member-mng:add")
    public Result<Void> save(@Validated @RequestBody MemberMngSaveRequest request) {
        return memberMngApi.save(request);
    }

    @Operation(summary = "修改成员")
    @OperateLog(title = "成员管理", subTitle = "修改成员", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update")
    @CheckTenantPerms("user-mod:member-mng:edit")
    public Result<Void> update(@Validated @RequestBody MemberMngUpdateRequest request) {
        return memberMngApi.update(request);
    }

    @Operation(summary = "重置密码")
    @OperateLog(title = "成员管理", subTitle = "重置密码", type = OperateTypeEnum.UPDATE)
    @PostMapping("/reset-password")
    @CheckTenantPerms("user-mod:member-mng:edit")
    public Result<Void> resetPwd(@RequestBody @Valid PwdResetRequest request) {
        return memberMngApi.resetPwd(request);
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "成员管理", subTitle = "状态修改", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update-status")
    @CheckTenantPerms("user-mod:member-mng:edit")
    public Result<Void> updateStatus(@RequestBody StatusUpdateRequest request) {
        return memberMngApi.updateStatus(request);
    }

    @Operation(summary = "授权职位")
    @OperateLog(title = "成员管理", subTitle = "授权成员职位", type = OperateTypeEnum.GRANT)
    @PostMapping("/grant-post")
    @CheckTenantPerms("user-mod:member-mng:grant-post")
    public Result<Void> grantPosts(@RequestBody @Valid MemberMngGrantPostRequest request) {
        return memberMngApi.grantPosts(request);
    }

    @Operation(summary = "删除成员")
    @OperateLog(title = "成员管理", subTitle = "删除成员", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckTenantPerms("user-mod:member-mng:remove")
    public Result<Void> remove(@RequestBody List<Long> memberIds) {
        return memberMngApi.remove(memberIds);
    }

}


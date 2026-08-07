package com.wzkris.usercenter.remote.controller.member;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.model.UserRole;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.member.MemberRemoteApi;
import com.wzkris.usercenter.remote.api.member.request.MemberPermsQueryRequest;
import com.wzkris.usercenter.remote.api.member.request.MemberQueryRequest;
import com.wzkris.usercenter.remote.api.member.request.TenantIdRequest;
import com.wzkris.usercenter.remote.api.member.response.MemberListResponse;
import com.wzkris.usercenter.remote.api.member.response.MemberQueryResponse;
import com.wzkris.usercenter.request.StringValueRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "会员")
@RestController
@RequestMapping("/member-remote")
@RequiredArgsConstructor
public class MemberRemoteController {

    private final MemberRemoteApi memberRemoteApi;

    @Operation(summary = "查询会员列表")
    @PostMapping("/query-list")
    public Result<List<MemberListResponse>> queryList(@RequestBody @Valid MemberQueryRequest request) {
        return memberRemoteApi.queryList(request);
    }

    @Operation(summary = "查询租户管理员")
    @PostMapping("/query-tenant-administrator")
    public Result<MemberQueryResponse> queryTenantAdministrator(@RequestBody @Valid TenantIdRequest request) {
        return memberRemoteApi.queryTenantAdministrator(request);
    }

    @Operation(summary = "根据微信code查询会员")
    @PostMapping("/query-by-wexcxcode")
    public Result<MemberQueryResponse> queryByWexcxCode(@RequestBody @Valid StringValueRequest request) {
        return memberRemoteApi.queryByWexcxCode(request);
    }

    @Operation(summary = "查询会员权限")
    @PostMapping("/query-permission")
    public Result<List<UserRole>> queryPermission(@RequestBody MemberPermsQueryRequest request) {
        return memberRemoteApi.queryPermission(request);
    }

    @Operation(summary = "更新会员登录信息")
    @PostMapping("/update-logininfo")
    public Result<Void> updateLoginInfo(@RequestBody LoginInfoUpdateRequest request) {
        return memberRemoteApi.updateLoginInfo(request);
    }

}

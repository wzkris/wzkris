package com.wzkris.usercenter.remote.controller.member;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.member.MemberInfoRemoteApi;
import com.wzkris.usercenter.remote.api.member.request.MemberPermsQueryRequest;
import com.wzkris.usercenter.remote.api.member.request.MemberQueryRequest;
import com.wzkris.usercenter.remote.api.member.request.TenantIdRequest;
import com.wzkris.usercenter.remote.api.member.response.MemberInfoResponse;
import com.wzkris.usercenter.remote.api.member.response.MemberPermissionResponse;
import com.wzkris.usercenter.request.StringValueRequest;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/member-info-remote")
@RequiredArgsConstructor
public class MemberInfoRemoteController {

    private final MemberInfoRemoteApi memberInfoRemoteApi;

    @PostMapping("/query-one")
    public Result<MemberInfoResponse> queryOne(@RequestBody @Valid MemberQueryRequest request) {
        return memberInfoRemoteApi.queryOne(request);
    }

    @PostMapping("/query-administrator-by-tenant")
    public Result<MemberInfoResponse> queryAdministratorByTenantId(@RequestBody @Valid TenantIdRequest request) {
        return memberInfoRemoteApi.queryAdministratorByTenantId(request);
    }

    @PostMapping("/query-by-wexcxcode")
    public Result<MemberInfoResponse> queryByWexcxCode(@RequestBody @Valid StringValueRequest request) {
        return memberInfoRemoteApi.queryByWexcxCode(request);
    }

    @PostMapping("/query-permission")
    public Result<MemberPermissionResponse> queryPermission(@RequestBody MemberPermsQueryRequest request) {
        return memberInfoRemoteApi.queryPermission(request);
    }

    @PostMapping("/update-logininfo")
    public Result<Void> updateLoginInfo(@RequestBody LoginInfoUpdateRequest request) {
        return memberInfoRemoteApi.updateLoginInfo(request);
    }

}


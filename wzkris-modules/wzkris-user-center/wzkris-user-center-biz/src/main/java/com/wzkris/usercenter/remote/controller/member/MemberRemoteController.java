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
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/member-remote")
@RequiredArgsConstructor
public class MemberRemoteController {

    private final MemberRemoteApi memberRemoteApi;

    @PostMapping("/query-list")
    public Result<List<MemberListResponse>> queryList(@RequestBody @Valid MemberQueryRequest request) {
        return memberRemoteApi.queryList(request);
    }

    @PostMapping("/query-tenant-administrator")
    public Result<MemberQueryResponse> queryTenantAdministrator(@RequestBody @Valid TenantIdRequest request) {
        return memberRemoteApi.queryTenantAdministrator(request);
    }

    @PostMapping("/query-by-wexcxcode")
    public Result<MemberQueryResponse> queryByWexcxCode(@RequestBody @Valid StringValueRequest request) {
        return memberRemoteApi.queryByWexcxCode(request);
    }

    @PostMapping("/query-permission")
    public Result<List<UserRole>> queryPermission(@RequestBody MemberPermsQueryRequest request) {
        return memberRemoteApi.queryPermission(request);
    }

    @PostMapping("/update-logininfo")
    public Result<Void> updateLoginInfo(@RequestBody LoginInfoUpdateRequest request) {
        return memberRemoteApi.updateLoginInfo(request);
    }

}

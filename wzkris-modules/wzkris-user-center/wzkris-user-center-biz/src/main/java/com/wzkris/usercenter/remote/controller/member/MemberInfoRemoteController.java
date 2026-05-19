package com.wzkris.usercenter.remote.controller.member;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.member.MemberInfoRemoteApi;
import com.wzkris.usercenter.remote.api.member.request.MemberPermsQueryRequest;
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

    @PostMapping("/query-by-username")
    public Result<MemberInfoResponse> queryByUsername(@RequestBody @Valid StringValueRequest request) {
        return memberInfoRemoteApi.queryByUsername(request);
    }

    @PostMapping("/query-by-phonenumber")
    public Result<MemberInfoResponse> queryByPhoneNumber(@RequestBody @Valid StringValueRequest request) {
        return memberInfoRemoteApi.queryByPhoneNumber(request);
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


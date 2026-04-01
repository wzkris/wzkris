package com.wzkris.usercenter.remote.controller.member;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.member.MemberInfoRemoteApi;
import com.wzkris.usercenter.remote.api.member.request.MemberPermsQueryRequest;
import com.wzkris.usercenter.remote.api.member.response.MemberInfoResponse;
import com.wzkris.usercenter.response.permission.MemberPermissionResponse;
import io.swagger.v3.oas.annotations.Hidden;
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
    public Result<MemberInfoResponse> getByUsername(@RequestBody String username) {
        return memberInfoRemoteApi.getByUsername(username);
    }

    @PostMapping("/query-by-phonenumber")
    public Result<MemberInfoResponse> getByPhoneNumber(@RequestBody String phoneNumber) {
        return memberInfoRemoteApi.getByPhoneNumber(phoneNumber);
    }

    @PostMapping("/query-by-wexcx-identifier")
    public Result<MemberInfoResponse> getByWexcxIdentifier(@RequestBody String xcxIdentifier) {
        return memberInfoRemoteApi.getByWexcxIdentifier(xcxIdentifier);
    }

    @PostMapping("/query-permission")
    public Result<MemberPermissionResponse> getPermission(@RequestBody MemberPermsQueryRequest memberPermsReq) {
        return memberInfoRemoteApi.getPermission(memberPermsReq);
    }

    @PostMapping("/update-logininfo")
    public Result<Void> updateLoginInfo(@RequestBody LoginInfoUpdateRequest loginInfoUpdateRequest) {
        return memberInfoRemoteApi.updateLoginInfo(loginInfoUpdateRequest);
    }

}






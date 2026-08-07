package com.wzkris.usercenter.controller.member;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.usercenter.api.member.MemberInfoApi;
import com.wzkris.usercenter.api.member.request.MemberInfoBasicUpdateRequest;
import com.wzkris.usercenter.api.member.response.MemberInfoQueryResponse;
import com.wzkris.usercenter.request.PasswordUpdateRequest;
import com.wzkris.usercenter.request.PhoneNumberUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "会员用户")
@RestController
@RequestMapping("/member-info")
@RequiredArgsConstructor
@CheckPerms(checkTypes = AuthTypeEnum.TENANT)
public class MemberInfoController {

    private final MemberInfoApi memberInfoApi;

    @Operation(summary = "账户信息")
    @GetMapping("/query")
    public Result<MemberInfoQueryResponse> query() {
        return memberInfoApi.query();
    }

    @Operation(summary = "修改基本信息")
    @OperateLog(title = "个人信息", subTitle = "修改基本信息", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update-basic")
    public Result<Void> updateBasicInfo(@RequestBody MemberInfoBasicUpdateRequest request) {
        return memberInfoApi.updateBasicInfo(request);
    }

    @Operation(summary = "修改手机号")
    @OperateLog(title = "个人信息", subTitle = "修改手机号", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update-phonenumber")
    public Result<Void> updatePhoneNumber(@RequestBody @Valid PhoneNumberUpdateRequest request) {
        return memberInfoApi.updatePhoneNumber(request);
    }

    @Operation(summary = "修改密码")
    @OperateLog(title = "个人信息", subTitle = "修改密码", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update-password")
    public Result<Void> updatePwd(@RequestBody @Validated(PasswordUpdateRequest.LoginPwd.class) PasswordUpdateRequest request) {
        return memberInfoApi.updatePwd(request);
    }

}


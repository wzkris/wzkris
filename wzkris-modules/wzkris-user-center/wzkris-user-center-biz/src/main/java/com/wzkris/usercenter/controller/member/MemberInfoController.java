package com.wzkris.usercenter.controller.member;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.usercenter.api.member.MemberInfoApi;
import com.wzkris.usercenter.request.PasswordUpdateRequest;
import com.wzkris.usercenter.request.PhoneNumberUpdateRequest;
import com.wzkris.usercenter.request.member.MemberInfoBasicUpdateRequest;
import com.wzkris.usercenter.response.member.MemberInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "租户成员信息")
@Validated
@RestController
@RequestMapping("/member-info")
@RequiredArgsConstructor
public class MemberInfoController {

    private final MemberInfoApi memberInfoApi;

    @Operation(summary = "账户信息")
    @GetMapping("/query-info")
    public Result<MemberInfoResponse> queryInfo() {
        return memberInfoApi.queryInfo();
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


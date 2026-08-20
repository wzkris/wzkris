package com.wzkris.usercenter.controller.tenantuser;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.usercenter.api.tenantuser.TenantUserInfoApi;
import com.wzkris.usercenter.api.tenantuser.request.TenantUserInfoBasicUpdateRequest;
import com.wzkris.usercenter.api.tenantuser.response.TenantUserInfoQueryResponse;
import com.wzkris.usercenter.request.PasswordUpdateRequest;
import com.wzkris.usercenter.request.PhoneNumberUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "租户用户")
@RestController
@RequestMapping("/tenant-user-info")
@RequiredArgsConstructor
@CheckPerms(checkTypes = AuthTypeEnum.TENANT)
public class TenantUserInfoController {

    private final TenantUserInfoApi tenantUserInfoApi;

    @Operation(summary = "账户信息")
    @GetMapping("/query")
    public Result<TenantUserInfoQueryResponse> query() {
        return tenantUserInfoApi.query();
    }

    @Operation(summary = "修改基本信息")
    @OperateLog(title = "个人信息", subTitle = "修改基本信息", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update-basic")
    public Result<Void> updateBasicInfo(@RequestBody TenantUserInfoBasicUpdateRequest request) {
        return tenantUserInfoApi.updateBasicInfo(request);
    }

    @Operation(summary = "修改手机号")
    @OperateLog(title = "个人信息", subTitle = "修改手机号", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update-phonenumber")
    public Result<Void> updatePhoneNumber(@RequestBody @Valid PhoneNumberUpdateRequest request) {
        return tenantUserInfoApi.updatePhoneNumber(request);
    }

    @Operation(summary = "修改密码")
    @OperateLog(title = "个人信息", subTitle = "修改密码", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update-password")
    public Result<Void> updatePwd(@RequestBody @Validated(PasswordUpdateRequest.LoginPwd.class) PasswordUpdateRequest request) {
        return tenantUserInfoApi.updatePwd(request);
    }

}


package com.wzkris.usercenter.controller.admin;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.usercenter.api.admin.AdminInfoApi;
import com.wzkris.usercenter.api.admin.request.AdminInfoBasicUpdateRequest;
import com.wzkris.usercenter.api.admin.response.AdminInfoQueryResponse;
import com.wzkris.usercenter.request.PasswordUpdateRequest;
import com.wzkris.usercenter.request.PhoneNumberUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "管理员信息")
@RestController
@RequestMapping("/admin-info")
@RequiredArgsConstructor
@CheckPerms(checkTypes = AuthTypeEnum.ADMIN)
public class AdminInfoController {

    private final String info_prefix = "userinfo";

    private final AdminInfoApi adminInfoApi;

    @Operation(summary = "账户信息")
    @GetMapping("/query-info")
    @Cacheable(value = info_prefix + "#600_000", key = "@uch.getLoginUser().getUid()", sync = true) // TODO 这里缓存的需要在退出时移除
    public Result<AdminInfoQueryResponse> queryInfo() {
        return adminInfoApi.queryInfo();
    }

    @Operation(summary = "修改基本信息")
    @OperateLog(title = "个人信息", subTitle = "修改基本信息", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update-basic")
    @CacheEvict(value = info_prefix, key = "@uch.getLoginUser().getUid()")
    public Result<Void> updateBasicInfo(@RequestBody AdminInfoBasicUpdateRequest request) {
        return adminInfoApi.updateBasicInfo(request);
    }

    @Operation(summary = "修改手机号")
    @OperateLog(title = "个人信息", subTitle = "修改手机号", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update-phonenumber")
    @CacheEvict(value = info_prefix, key = "@uch.getLoginUser().getUid()")
    public Result<Void> updatePhoneNumber(@RequestBody @Valid PhoneNumberUpdateRequest request) {
        return adminInfoApi.updatePhoneNumber(request);
    }

    @Operation(summary = "修改密码")
    @OperateLog(title = "个人信息", subTitle = "修改密码", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update-password")
    public Result<Void> updatePwd(@RequestBody @Validated(PasswordUpdateRequest.LoginPwd.class) PasswordUpdateRequest request) {
        return adminInfoApi.updatePwd(request);
    }

}


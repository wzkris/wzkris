package com.wzkris.user.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.domain.req.XcxRegisterReq;
import com.wzkris.user.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户注册
 *
 * @author wzkris
 */
@Tag(name = "app注册")
@Slf4j
@Validated
@RestController
@RequestMapping("/app/register_by")
@RequiredArgsConstructor
public class AppRegisterController extends BaseController {

    private final AppUserService appUserService;

    @Operation(summary = "账户注册-小程序")
    @PostMapping("xcx")
    public Result<Void> register(@RequestBody @Valid XcxRegisterReq req) {
        appUserService.registerByXcx(req.getJscode(), req.getCode());
        return ok();
    }

    @Operation(summary = "账户注册-公众号")
    @PostMapping("gzh")
    public Result<Void> register(@RequestBody @NotBlank(message = "[code] {validate.notnull}")
                                 String code) {
        appUserService.registerByGzh(code);
        return ok();
    }
}

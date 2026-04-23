package com.wzkris.auth.controller;

import com.wzkris.auth.api.SwitchTokenApi;
import com.wzkris.auth.request.WexcxSwitchRequest;
import com.wzkris.common.core.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "token切换控制器")
@Slf4j
@RestController
@RequestMapping("/switching-token")
@RequiredArgsConstructor
public class SwitchTokenController {

    private final SwitchTokenApi switchTokenApi;

    @Operation(summary = "微信小程序-切换到租户Token")
    @PostMapping("/wexcx/to-tenant")
    public Result<?> tenantToken(@RequestBody @Validated WexcxSwitchRequest switchReq) {
        return switchTokenApi.switchTenantToken(switchReq);
    }

}


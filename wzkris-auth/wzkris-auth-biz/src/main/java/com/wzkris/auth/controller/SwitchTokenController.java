package com.wzkris.auth.controller;

import com.wzkris.auth.domain.req.WexcxSwitchReq;
import com.wzkris.common.core.model.Result;
import com.wzkris.principal.feign.member.MemberInfoFeign;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.wzkris.common.core.model.Result.ok;

@Tag(name = "token切换控制器")
@Slf4j
@RestController
@RequestMapping("/switching-token")
@RequiredArgsConstructor
public class SwitchTokenController {

    private final MemberInfoFeign memberInfoFeign;

    @Operation(summary = "小程序-切换到租户Token")
    @PostMapping("/xcx/to-tenant")
    public Result<Void> tenantToken(@RequestBody @Validated WexcxSwitchReq switchReq) {
//        memberInfoFeign.getByWexcxIdentifier();
        return ok();
    }

}

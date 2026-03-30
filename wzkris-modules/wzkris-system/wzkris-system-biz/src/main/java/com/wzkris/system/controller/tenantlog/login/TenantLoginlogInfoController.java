package com.wzkris.system.controller.tenantlog.login;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.api.tenantlog.login.TenantLoginlogInfoApi;
import com.wzkris.system.request.tenantlog.TenantLoginLogQueryRequest;
import com.wzkris.system.response.tenantlog.TenantLoginLogResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "租户个人登录日志信息")
@RestController
@RequestMapping("/tenant-loginlog-info")
@RequiredArgsConstructor
public class TenantLoginlogInfoController {

    private final TenantLoginlogInfoApi tenantLoginlogInfoApi;

    @Operation(summary = "分页")
    @GetMapping("/query-page")
    public Result<Page<TenantLoginLogResponse>> queryPage(TenantLoginLogQueryRequest request) {
        return tenantLoginlogInfoApi.queryPage(request);
    }

}


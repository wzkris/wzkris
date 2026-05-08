package com.wzkris.system.controller.tenantlog.login;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckTenantPerms;
import com.wzkris.system.api.tenantlog.login.TenantLoginlogMngApi;
import com.wzkris.system.request.tenantlog.TenantLoginLogMngPageRequest;
import com.wzkris.system.response.tenantlog.TenantLoginLogMngResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "租户登录日志管理")
@RestController
@RequestMapping("/tenant-loginlog-manage")
@RequiredArgsConstructor
public class TenantLoginlogMngController {

    private final TenantLoginlogMngApi tenantLoginlogMngApi;

    @Operation(summary = "分页")
    @GetMapping("/query-page")
    @CheckTenantPerms("system-mod:tenant-loginlog-mng:page")
    public Result<Page<TenantLoginLogMngResponse>> queryPage(@ParameterObject TenantLoginLogMngPageRequest request) {
        return tenantLoginlogMngApi.queryPage(request);
    }

}

package com.wzkris.usercenter.controller.tenantlog.login;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.usercenter.api.tenantlog.login.TenantLoginlogMngApi;
import com.wzkris.usercenter.api.tenantlog.login.request.TenantLoginLogMngPageRequest;
import com.wzkris.usercenter.api.tenantlog.login.response.TenantLoginLogMngResponse;
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
    @CheckPerms(checkTypes = AuthTypeEnum.TENANT, value = "system-mod:tenant-loginlog-mng:page")
    public Result<Page<TenantLoginLogMngResponse>> queryPage(@ParameterObject TenantLoginLogMngPageRequest request) {
        return tenantLoginlogMngApi.queryPage(request);
    }

}

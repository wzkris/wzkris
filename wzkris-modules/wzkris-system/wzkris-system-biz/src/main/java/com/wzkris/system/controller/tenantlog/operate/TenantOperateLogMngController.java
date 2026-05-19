package com.wzkris.system.controller.tenantlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckTenantPerms;
import com.wzkris.system.api.tenantlog.operate.TenantOperateLogMngApi;
import com.wzkris.system.api.tenantlog.request.TenantOperateLogMngPageRequest;
import com.wzkris.system.api.tenantlog.response.TenantOperateLogMngResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "租户操作日志管理")
@RestController
@RequestMapping("/tenant-operatelog-manage")
@RequiredArgsConstructor
public class TenantOperateLogMngController {

    private final TenantOperateLogMngApi tenantOperateLogMngApi;

    @Operation(summary = "分页")
    @GetMapping("/query-page")
    @CheckTenantPerms("system-mod:tenant-operatelog-mng:page")
    public Result<Page<TenantOperateLogMngResponse>> queryPage(@ParameterObject TenantOperateLogMngPageRequest request) {
        return tenantOperateLogMngApi.queryPage(request);
    }

}

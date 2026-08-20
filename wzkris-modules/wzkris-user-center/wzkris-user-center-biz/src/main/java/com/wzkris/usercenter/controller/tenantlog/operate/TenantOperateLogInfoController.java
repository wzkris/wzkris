package com.wzkris.usercenter.controller.tenantlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.tenantlog.operate.TenantOperateLogInfoApi;
import com.wzkris.usercenter.api.tenantlog.operate.request.TenantOperateLogInfoPageRequest;
import com.wzkris.usercenter.api.tenantlog.operate.response.TenantOperateLogInfoPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "租户个人操作日志信息")
@RestController
@RequestMapping("/tenant-operatelog-info")
@RequiredArgsConstructor
public class TenantOperateLogInfoController {

    private final TenantOperateLogInfoApi tenantOperateLogInfoApi;

    @Operation(summary = "分页")
    @GetMapping("/query-page")
    public Result<Page<TenantOperateLogInfoPageResponse>> queryPage(@ParameterObject TenantOperateLogInfoPageRequest request) {
        return tenantOperateLogInfoApi.queryPage(request);
    }

}

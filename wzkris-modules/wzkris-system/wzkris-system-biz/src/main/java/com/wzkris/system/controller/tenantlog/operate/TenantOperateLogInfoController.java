package com.wzkris.system.controller.tenantlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.api.tenantlog.operate.TenantOperateLogInfoApi;
import com.wzkris.system.request.tenantlog.TenantOperateLogQueryRequest;
import com.wzkris.system.response.tenantlog.TenantOperateLogInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "租户个人操作日志信息")
@RestController
@RequestMapping("/tenant-operatelog-info")
@RequiredArgsConstructor
public class TenantOperateLogInfoController extends BaseController {

    private final TenantOperateLogInfoApi tenantOperateLogInfoApi;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<Page<TenantOperateLogInfoResponse>> page(TenantOperateLogQueryRequest request) {
        return tenantOperateLogInfoApi.page(request);
    }

}


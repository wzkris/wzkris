package com.wzkris.usercenter.controller.tenantlog.login;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.tenantlog.login.TenantLoginlogInfoApi;
import com.wzkris.usercenter.api.tenantlog.login.request.TenantLoginLogInfoPageRequest;
import com.wzkris.usercenter.api.tenantlog.login.response.TenantLoginLogInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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
    public Result<Page<TenantLoginLogInfoResponse>> queryPage(@ParameterObject TenantLoginLogInfoPageRequest request) {
        return tenantLoginlogInfoApi.queryPage(request);
    }

}

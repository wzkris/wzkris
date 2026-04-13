package com.wzkris.usercenter.controller.menu;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.usercenter.api.menu.MenuInfoApi;
import com.wzkris.usercenter.response.RouterResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "菜单路由")
@RestController
@RequestMapping("/menu-info")
@RequiredArgsConstructor
public class MenuInfoController {

    private final MenuInfoApi menuInfoApi;

    @Operation(summary = "系统路由")
    @GetMapping("/query-system-route")
    public Result<List<RouterResponse>> querySystemRoute() {
        return menuInfoApi.querySystemRoute(SecurityUtil.getUid());
    }

    @Operation(summary = "租户路由")
    @GetMapping("/query-tenant-route")
    public Result<List<RouterResponse>> queryTenantRoute() {
        return menuInfoApi.queryTenantRoute(SecurityUtil.getUid());
    }

}


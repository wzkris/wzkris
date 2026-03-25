package com.wzkris.usercenter.impl.menu;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.usercenter.api.menu.MenuInfoApi;
import com.wzkris.usercenter.response.RouterResponse;
import com.wzkris.usercenter.service.MenuInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuInfoApiImpl extends BaseController implements MenuInfoApi {

    private final MenuInfoService menuInfoService;

    @Override
    public Result<List<RouterResponse>> querySystemRoute(Long uid) {
        List<RouterResponse> routerVOS = menuInfoService.listSystemRoutes(uid);
        return ok(routerVOS);
    }

    @Override
    public Result<List<RouterResponse>> queryTenantRoute(Long uid) {
        List<RouterResponse> routerVOS = menuInfoService.listTenantRoutes(uid);
        return ok(routerVOS);
    }

}

package com.wzkris.usercenter.api.menu;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.response.RouterResponse;

import java.util.List;

public interface MenuInfoApi {

    Result<List<RouterResponse>> querySystemRoute(Long uid);

    Result<List<RouterResponse>> queryTenantRoute(Long uid);

}

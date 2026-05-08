package com.wzkris.usercenter.api.menu;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.request.menu.MenuMngListRequest;
import com.wzkris.usercenter.request.menu.MenuMngSaveRequest;
import com.wzkris.usercenter.request.menu.MenuMngUpdateRequest;
import com.wzkris.usercenter.response.menu.MenuMngResponse;

import java.util.List;

public interface MenuMngApi {

    Result<List<MenuMngResponse>> queryList(MenuMngListRequest request);

    Result<MenuMngResponse> queryInfo(IdRequest request);

    Result<Void> save(MenuMngSaveRequest request);

    Result<Void> update(MenuMngUpdateRequest request);

    Result<Void> remove(IdRequest request);

}

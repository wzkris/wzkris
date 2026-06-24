package com.wzkris.usercenter.api.menu;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.api.menu.request.MenuMngTreeRequest;
import com.wzkris.usercenter.api.menu.request.MenuMngSaveRequest;
import com.wzkris.usercenter.api.menu.request.MenuMngUpdateRequest;
import com.wzkris.usercenter.api.menu.response.MenuMngResponse;

import java.util.List;

public interface MenuMngApi {

    Result<List<MenuMngResponse>> queryList(MenuMngTreeRequest request);

    Result<MenuMngResponse> queryInfo(IdRequest request);

    Result<Void> save(MenuMngSaveRequest request);

    Result<Void> update(MenuMngUpdateRequest request);

    Result<Void> remove(IdRequest request);

}

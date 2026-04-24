package com.wzkris.usercenter.api.menu;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.request.common.IdRequest;
import com.wzkris.usercenter.request.menu.MenuMngListRequest;
import com.wzkris.usercenter.request.menu.MenuMngSaveRequest;
import com.wzkris.usercenter.request.menu.MenuMngUpdateRequest;
import com.wzkris.usercenter.response.menu.MenuInfoResponse;

import java.util.List;

public interface MenuMngApi {

    Result<List<MenuInfoResponse>> queryList(MenuMngListRequest request);

    Result<MenuInfoResponse> queryInfo(IdRequest request);

    Result<Void> save(MenuMngSaveRequest request);

    Result<Void> update(MenuMngUpdateRequest request);

    Result<Void> remove(IdRequest request);

}

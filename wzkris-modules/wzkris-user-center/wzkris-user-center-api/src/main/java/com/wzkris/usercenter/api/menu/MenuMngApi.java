package com.wzkris.usercenter.api.menu;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.request.StatusUpdateRequest;
import com.wzkris.usercenter.request.menu.MenuMngQueryRequest;
import com.wzkris.usercenter.request.menu.MenuMngSaveRequest;
import com.wzkris.usercenter.request.menu.MenuMngUpdateRequest;
import com.wzkris.usercenter.response.menu.MenuInfoResponse;

import java.util.List;

public interface MenuMngApi {

    Result<List<MenuInfoResponse>> queryList(MenuMngQueryRequest request);

    Result<MenuInfoResponse> queryInfo(Long menuId);

    Result<Void> save(MenuMngSaveRequest request);

    Result<Void> update(MenuMngUpdateRequest request);

    Result<Void> editStatus(StatusUpdateRequest request);

    Result<Void> remove(Long menuId);

}

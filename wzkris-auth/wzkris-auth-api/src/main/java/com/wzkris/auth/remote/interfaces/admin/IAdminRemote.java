package com.wzkris.auth.remote.interfaces.admin;

import com.wzkris.auth.remote.interfaces.admin.request.AdminPermissionQueryRequest;
import com.wzkris.auth.remote.interfaces.admin.request.AdminQueryRequest;
import com.wzkris.auth.remote.interfaces.admin.request.LoginInfoUpdateRequest;
import com.wzkris.auth.remote.interfaces.admin.response.AdminListResponse;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.model.UserRole;
import com.wzkris.common.remote.annotation.RemoteInterface;
import com.wzkris.common.remote.constants.ServiceContextPathConstant;
import com.wzkris.common.remote.constants.ServiceIdConstant;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 用户管理
 * @date : 2024/4/15 16:20
 */
@RemoteInterface(
        serviceId = ServiceIdConstant.USER_CENTER,
        path = ServiceContextPathConstant.USER_CENTER
)
@HttpExchange(url = "/admin-remote")
public interface IAdminRemote {

    @PostExchange("/query-list")
    Result<List<AdminListResponse>> queryList(@RequestBody AdminQueryRequest request);

    /**
     * 查询管理员权限
     */
    @PostExchange("/query-permission")
    Result<List<UserRole>> queryPermission(@RequestBody AdminPermissionQueryRequest request);

    /**
     * 更新用户登录信息
     */
    @PostExchange("/update-logininfo")
    Result<Void> updateLoginInfo(@RequestBody LoginInfoUpdateRequest request);

}


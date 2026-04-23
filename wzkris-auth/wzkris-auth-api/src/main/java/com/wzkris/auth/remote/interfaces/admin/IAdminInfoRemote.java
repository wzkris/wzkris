package com.wzkris.auth.remote.interfaces.admin;

import com.wzkris.auth.remote.interfaces.admin.request.AdminPermsQueryRequest;
import com.wzkris.auth.remote.interfaces.admin.request.LoginInfoUpdateRequest;
import com.wzkris.auth.remote.interfaces.admin.response.AdminInfoResponse;
import com.wzkris.auth.remote.interfaces.admin.response.AdminPermissionResponse;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.remote.annotation.RemoteInterface;
import com.wzkris.common.remote.constants.ServiceContextPathConstant;
import com.wzkris.common.remote.constants.ServiceIdConstant;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

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
@HttpExchange(url = "/admin-info-remote")
public interface IAdminInfoRemote {

    /**
     * 根据用户名查询用户
     */
    @PostExchange("/query-by-username")
    Result<AdminInfoResponse> queryByUsername(@RequestBody String username);

    /**
     * 根据手机号查询用户
     */
    @PostExchange("/query-by-phonenumber")
    Result<AdminInfoResponse> queryByPhoneNumber(@RequestBody String phoneNumber);

    /**
     * 查询管理员权限
     */
    @PostExchange("/query-permission")
    Result<AdminPermissionResponse> queryPermission(@RequestBody AdminPermsQueryRequest AdminPermsQueryRequest);

    /**
     * 更新用户登录信息
     */
    @PostExchange("/update-logininfo")
    Result<Void> updateLoginInfo(@RequestBody LoginInfoUpdateRequest LoginInfoUpdateRequest);

}


package com.wzkris.auth.remote.admin;

import com.wzkris.auth.remote.admin.req.AdminPermsQueryReq;
import com.wzkris.auth.remote.admin.req.LoginInfoUpdateReq;
import com.wzkris.auth.remote.admin.resp.AdminInfoResp;
import com.wzkris.auth.remote.admin.resp.AdminPermissionResp;
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
@HttpExchange(url = "/admin-info-client")
public interface IAdminInfoRemote {

    /**
     * 根据用户名查询用户
     */
    @PostExchange("/query-by-username")
    Result<AdminInfoResp> getByUsername(@RequestBody String username);

    /**
     * 根据手机号查询用户
     */
    @PostExchange("/query-by-phonenumber")
    Result<AdminInfoResp> getByPhoneNumber(@RequestBody String phoneNumber);

    /**
     * 查询管理员权限
     */
    @PostExchange("/query-permission")
    Result<AdminPermissionResp> getPermission(@RequestBody AdminPermsQueryReq adminPermsQueryReq);

    /**
     * 更新用户登录信息
     */
    @PostExchange("/update-logininfo")
    Result<Void> updateLoginInfo(@RequestBody LoginInfoUpdateReq loginInfoUpdateReq);

}

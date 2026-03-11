package com.wzkris.usercenter.httpclient.admin;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.httpclient.annotation.HttpClient;
import com.wzkris.common.httpclient.constants.ServiceContextPathConstant;
import com.wzkris.common.httpclient.constants.ServiceIdConstant;
import com.wzkris.usercenter.httpclient.admin.req.LoginInfoReq;
import com.wzkris.usercenter.httpclient.admin.req.QueryAdminPermsReq;
import com.wzkris.usercenter.httpclient.admin.resp.AdminInfoResp;
import com.wzkris.usercenter.httpclient.admin.resp.AdminPermissionResp;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 用户管理
 * @date : 2024/4/15 16:20
 */
@HttpClient(
        serviceId = ServiceIdConstant.USER_CENTER,
        path = ServiceContextPathConstant.USER_CENTER
)
@HttpExchange(url = "/admin-info-client")
public interface AdminInfoClient {

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
    Result<AdminPermissionResp> getPermission(@RequestBody QueryAdminPermsReq queryAdminPermsReq);

    /**
     * 更新用户登录信息
     */
    @PostExchange("/update-logininfo")
    Result<Void> updateLoginInfo(@RequestBody LoginInfoReq loginInfoReq);

}

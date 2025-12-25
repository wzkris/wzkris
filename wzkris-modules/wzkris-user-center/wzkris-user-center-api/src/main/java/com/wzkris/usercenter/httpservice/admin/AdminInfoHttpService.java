package com.wzkris.usercenter.httpservice.admin;

import com.wzkris.common.httpservice.annotation.HttpServiceClient;
import com.wzkris.common.httpservice.constants.ServiceIdConstant;
import com.wzkris.usercenter.httpservice.admin.fallback.AdminInfoHttpServiceFallback;
import com.wzkris.usercenter.httpservice.admin.req.LoginInfoReq;
import com.wzkris.usercenter.httpservice.admin.req.QueryAdminPermsReq;
import com.wzkris.usercenter.httpservice.admin.resp.AdminPermissionResp;
import com.wzkris.usercenter.httpservice.admin.resp.adminInfoResp;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 用户管理
 * @date : 2024/4/15 16:20
 */
@HttpServiceClient(
        serviceId = ServiceIdConstant.USER_CENTER,
        fallbackFactory = AdminInfoHttpServiceFallback.class
)
@HttpExchange(url = "/feign-admininfo")
public interface AdminInfoHttpService {

    /**
     * 根据用户名查询用户
     */
    @PostExchange("/query-by-username")
    adminInfoResp getByUsername(@RequestBody String username);

    /**
     * 根据手机号查询用户
     */
    @PostExchange("/query-by-phonenumber")
    adminInfoResp getByPhoneNumber(@RequestBody String phoneNumber);

    /**
     * 查询管理员权限
     */
    @PostExchange("/query-permission")
    AdminPermissionResp getPermission(@RequestBody QueryAdminPermsReq queryAdminPermsReq);

    /**
     * 更新用户登录信息
     */
    @PostExchange("/update-logininfo")
    void updateLoginInfo(@RequestBody LoginInfoReq loginInfoReq);

}

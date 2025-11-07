package com.wzkris.principal.feign.admin;

import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import com.wzkris.principal.feign.admin.fallback.AdminInfoFeignFallback;
import com.wzkris.principal.feign.admin.req.LoginInfoReq;
import com.wzkris.principal.feign.admin.req.QueryAdminPermsReq;
import com.wzkris.principal.feign.admin.resp.AdminPermissionResp;
import com.wzkris.principal.feign.admin.resp.adminInfoResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 用户管理
 * @date : 2024/4/15 16:20
 */
@FeignClient(name = ServiceIdConstant.PRINCIPAL, contextId = "AdminInfoFeign",
        fallbackFactory = AdminInfoFeignFallback.class,
        path = "/feign-admininfo")
public interface AdminInfoFeign {

    /**
     * 根据用户名查询用户
     */
    @PostMapping("/query-by-username")
    adminInfoResp getByUsername(@RequestBody String username);

    /**
     * 根据手机号查询用户
     */
    @PostMapping("/query-by-phonenumber")
    adminInfoResp getByPhoneNumber(@RequestBody String phoneNumber);

    /**
     * 查询管理员权限
     */
    @PostMapping("/query-permission")
    AdminPermissionResp getPermission(@RequestBody QueryAdminPermsReq queryAdminPermsReq);

    /**
     * 更新用户登录信息
     */
    @PostMapping("/update-logininfo")
    void updateLoginInfo(@RequestBody LoginInfoReq loginInfoReq);

}

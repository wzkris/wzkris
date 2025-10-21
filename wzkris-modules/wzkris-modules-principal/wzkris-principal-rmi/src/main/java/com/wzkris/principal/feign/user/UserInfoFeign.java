package com.wzkris.principal.feign.user;

import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import com.wzkris.principal.feign.user.fallback.UserInfoFeignFallback;
import com.wzkris.principal.feign.user.req.LoginInfoReq;
import com.wzkris.principal.feign.user.req.QueryUserPermsReq;
import com.wzkris.principal.feign.user.resp.UserInfoResp;
import com.wzkris.principal.feign.user.resp.UserPermissionResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 用户管理
 * @date : 2024/4/15 16:20
 */
@FeignClient(name = ServiceIdConstant.PRINCIPAL, contextId = "UserInfoFeign",
        fallbackFactory = UserInfoFeignFallback.class,
        path = "/feign-userinfo")
public interface UserInfoFeign {

    /**
     * 根据用户名查询用户
     */
    @PostMapping("/query-by-username")
    UserInfoResp getByUsername(@RequestBody String username);

    /**
     * 根据手机号查询用户
     */
    @PostMapping("/query-by-phonenumber")
    UserInfoResp getByPhoneNumber(@RequestBody String phoneNumber);

    /**
     * 查询管理员权限
     */
    @PostMapping("/query-permission")
    UserPermissionResp getPermission(@RequestBody QueryUserPermsReq queryUserPermsReq);

    /**
     * 更新用户登录信息
     */
    @PostMapping("/update-logininfo")
    void updateLoginInfo(@RequestBody LoginInfoReq loginInfoReq);

}

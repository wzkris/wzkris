package com.wzkris.user.feign.userinfo;

import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import com.wzkris.common.openfeign.core.RmiFeign;
import com.wzkris.user.feign.userinfo.fallback.UserInfoFeignFallback;
import com.wzkris.user.feign.userinfo.req.LoginInfoReq;
import com.wzkris.user.feign.userinfo.req.QueryPermsReq;
import com.wzkris.user.feign.userinfo.resp.PermissionResp;
import com.wzkris.user.feign.userinfo.resp.UserInfoResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 系统用户
 * @date : 2024/4/15 16:20
 */
@FeignClient(name = ServiceIdConstant.USER, contextId = "UserInfoFeign",
        fallbackFactory = UserInfoFeignFallback.class,
        path = "/feign-userinfo")
public interface UserInfoFeign extends RmiFeign {

    /**
     * 根据用户名查询系统用户
     */
    @PostMapping("/query-by-username")
    UserInfoResp getByUsername(@RequestBody String username);

    /**
     * 根据手机号查询系统用户
     */
    @PostMapping("/query-by-phonenumber")
    UserInfoResp getByPhoneNumber(@RequestBody String phoneNumber);

    /**
     * 查询管理员权限
     */
    @PostMapping("/query-permission")
    PermissionResp getPermission(@RequestBody QueryPermsReq queryPermsReq);

    /**
     * 更新用户登录信息
     */
    @PostMapping("/update-logininfo")
    void updateLoginInfo(@RequestBody LoginInfoReq loginInfoReq);

}

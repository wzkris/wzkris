package com.wzkris.user.rmi;

import com.wzkris.common.openfeign.constants.ServiceContextPathConstant;
import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import com.wzkris.common.openfeign.core.RmiFeign;
import com.wzkris.user.rmi.domain.req.LoginInfoReq;
import com.wzkris.user.rmi.domain.req.QueryPermsReq;
import com.wzkris.user.rmi.domain.resp.SysPermissionResp;
import com.wzkris.user.rmi.domain.resp.SysUserResp;
import com.wzkris.user.rmi.fallback.SysUserFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 系统用户
 * @date : 2024/4/15 16:20
 */
@FeignClient(name = ServiceIdConstant.USER, contextId = "SysUserFeign",
        fallbackFactory = SysUserFeignFallback.class,
        path = ServiceContextPathConstant.USER + "/feign-sys-user")
public interface SysUserFeign extends RmiFeign {

    /**
     * 根据用户名查询系统用户
     */
    @PostMapping("/query-by-username")
    SysUserResp getByUsername(@RequestBody String username);

    /**
     * 根据手机号查询系统用户
     */
    @PostMapping("/query-by-phonenumber")
    SysUserResp getByPhoneNumber(@RequestBody String phoneNumber);

    /**
     * 查询管理员权限
     */
    @PostMapping("/query-permission")
    SysPermissionResp getPermission(@RequestBody QueryPermsReq queryPermsReq);

    /**
     * 更新用户登录信息
     */
    @PostMapping("/update-logininfo")
    void updateLoginInfo(@RequestBody LoginInfoReq loginInfoReq);

}

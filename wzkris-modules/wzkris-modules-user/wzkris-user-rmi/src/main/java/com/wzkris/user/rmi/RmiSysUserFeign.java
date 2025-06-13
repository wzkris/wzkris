package com.wzkris.user.rmi;

import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import com.wzkris.common.openfeign.core.RmiFeign;
import com.wzkris.user.rmi.domain.req.LoginInfoReq;
import com.wzkris.user.rmi.domain.req.QueryPermsReq;
import com.wzkris.user.rmi.domain.resp.SysPermissionResp;
import com.wzkris.user.rmi.domain.resp.SysUserResp;
import com.wzkris.user.rmi.fallback.RmiSysUserFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 系统用户
 * @date : 2024/4/15 16:20
 */
@FeignClient(name = ServiceIdConstant.USER, contextId = "RmiSysUserFeign", fallbackFactory = RmiSysUserFeignFallback.class)
public interface RmiSysUserFeign extends RmiFeign {

    String prefix = "/rmi_sys_user";

    /**
     * 根据用户名查询系统用户
     */
    @PostMapping(prefix + "/get_by_username")
    SysUserResp getByUsername(@RequestBody String username);

    /**
     * 根据手机号查询系统用户
     */
    @PostMapping(prefix + "/get_by_phonenumber")
    SysUserResp getByPhoneNumber(@RequestBody String phoneNumber);

    /**
     * 查询管理员权限
     */
    @PostMapping(prefix + "/get_permission")
    SysPermissionResp getPermission(@RequestBody QueryPermsReq queryPermsReq);

    /**
     * 更新用户登录信息
     */
    @PostMapping(prefix + "/update_login_info")
    void updateLoginInfo(@RequestBody LoginInfoReq loginInfoReq);

}

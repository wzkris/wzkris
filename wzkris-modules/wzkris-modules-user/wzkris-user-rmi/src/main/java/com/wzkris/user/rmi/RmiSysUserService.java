package com.wzkris.user.rmi;

import com.wzkris.user.rmi.domain.req.LoginInfoReq;
import com.wzkris.user.rmi.domain.req.QueryPermsReq;
import com.wzkris.user.rmi.domain.resp.SysPermissionResp;
import com.wzkris.user.rmi.domain.resp.SysUserResp;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 系统用户
 * @date : 2024/4/15 16:20
 */
public interface RmiSysUserService {

    /**
     * 根据用户名查询系统用户
     */
    SysUserResp getByUsername(String username);

    /**
     * 根据手机号查询系统用户
     */
    SysUserResp getByPhoneNumber(String phoneNumber);

    /**
     * 查询管理员权限
     */
    SysPermissionResp getPermission(QueryPermsReq queryPermsReq);

    /**
     * 更新用户登录信息
     */
    void updateLoginInfo(LoginInfoReq loginInfoReq);
}

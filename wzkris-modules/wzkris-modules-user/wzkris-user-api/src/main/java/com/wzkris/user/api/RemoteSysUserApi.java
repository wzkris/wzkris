package com.wzkris.user.api;

import com.wzkris.user.api.domain.request.LoginInfoReq;
import com.wzkris.user.api.domain.request.QueryPermsReq;
import com.wzkris.user.api.domain.response.SysPermissionResp;
import com.wzkris.user.api.domain.response.SysUserResp;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 系统用户
 * @date : 2024/4/15 16:20
 */
public interface RemoteSysUserApi {

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

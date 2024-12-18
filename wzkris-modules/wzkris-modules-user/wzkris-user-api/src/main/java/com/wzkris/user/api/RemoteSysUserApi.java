package com.wzkris.user.api;

import com.wzkris.common.core.constant.ApplicationNameConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.user.api.domain.dto.LoginInfoDTO;
import com.wzkris.user.api.domain.dto.QueryPermsDTO;
import com.wzkris.user.api.domain.dto.SysPermissionDTO;
import com.wzkris.user.api.domain.dto.SysUserDTO;
import com.wzkris.user.api.fallback.RemoteSysUserApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import static com.wzkris.common.core.constant.SecurityConstants.INNER_NOAUTH_REQUEST_PATH;


/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 系统用户
 * @date : 2024/4/15 16:20
 */
@FeignClient(value = ApplicationNameConstants.USER, contextId = "RemoteSysUserApi",
        fallbackFactory = RemoteSysUserApiFallback.class)
public interface RemoteSysUserApi {

    /**
     * 根据用户名查询系统用户
     */
    @GetMapping(INNER_NOAUTH_REQUEST_PATH + "/query_sys_user_by_username")
    Result<SysUserDTO> getByUsername(@RequestParam("username") String username);

    /**
     * 查询管理员权限
     */
    @PostMapping(INNER_NOAUTH_REQUEST_PATH + "/query_sys_permission")
    Result<SysPermissionDTO> getPermission(@RequestBody QueryPermsDTO queryPermsDTO);

    /**
     * 更新用户登录信息
     */
    @PostMapping(INNER_NOAUTH_REQUEST_PATH + "/update_sys_user_logininfo")
    void updateLoginInfo(@RequestBody LoginInfoDTO loginInfoDTO);
}

package com.wzkris.user.api;

import com.wzkris.common.core.domain.Result;
import com.wzkris.user.api.domain.dto.LoginInfoDTO;
import com.wzkris.user.api.domain.dto.SysPermissionDTO;
import com.wzkris.user.api.domain.dto.SysUserDTO;
import com.wzkris.user.api.domain.vo.RouterVO;
import com.wzkris.user.api.fallback.RemoteSysUserApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.wzkris.common.core.constant.SecurityConstants.INNER_REQUEST_PATH;


/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 系统用户
 * @date : 2024/4/15 16:20
 */
@FeignClient(value = "wzkris-user", contextId = "RemoteSysUserApi",
        fallbackFactory = RemoteSysUserApiFallback.class)
public interface RemoteSysUserApi {

    /**
     * 根据用户名查询系统用户
     */
    @GetMapping(INNER_REQUEST_PATH + "/query_sys_user_by_username")
    Result<SysUserDTO> getByUsername(@RequestParam("username") String username);

    /**
     * 查询管理员权限
     */
    @GetMapping(INNER_REQUEST_PATH + "/query_sys_permission")
    Result<SysPermissionDTO> getPermission(@RequestParam("userId") Long userId,
                                           @RequestParam(value = "deptId", required = false) Long deptId);

    /**
     * 更新用户登录信息
     */
    @PostMapping(INNER_REQUEST_PATH + "/update_sys_user_logininfo")
    void updateLoginInfo(@RequestBody LoginInfoDTO loginInfoDTO);

    /**
     * 获取系统用户前端路由
     */
    @GetMapping(INNER_REQUEST_PATH + "/query_sys_user_router")
    Result<List<RouterVO>> getRouter(@RequestParam Long userId);
}

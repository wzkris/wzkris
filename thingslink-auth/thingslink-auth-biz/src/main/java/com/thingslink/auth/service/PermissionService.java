package com.thingslink.auth.service;

import com.thingslink.auth.domain.SysUser;
import com.thingslink.auth.domain.bo.PermissionBO;

import java.util.List;

/**
 * 权限信息 服务层
 *
 * @author wzkris
 */
public interface PermissionService {

    /**
     * 返回已授权码及数据权限
     *
     * @param user 系统用户
     * @return 权限
     */
    PermissionBO getPermission(SysUser user);

}

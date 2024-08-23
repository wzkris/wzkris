package com.wzkris.user.api.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : sys用户权限传输层
 * @date : 2024/4/16 09:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysPermissionDTO {
    /**
     * 是否当前租户下的超级管理员
     */
    private Boolean administrator;
    /**
     * 已授权限
     */
    private List<String> grantedAuthority;
    /**
     * 部门数据权限
     */
    private List<Long> deptScopes;
}

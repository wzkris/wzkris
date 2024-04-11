package com.thingslink.auth.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 权限业务层
 * @date : 2023/6/2 16:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionBO {
    /**
     * 是否当前租户下的超级管理员
     */
    private Boolean isAdmin;
    /**
     * 已授权限
     */
    private List<String> grantedAuthority;
    /**
     * 部门数据权限
     */
    private List<Long> deptScopes;
}

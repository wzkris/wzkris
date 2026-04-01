package com.wzkris.auth.remote.interfaces.admin.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 用户权限传输层
 * @date : 2024/4/16 09:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminPermissionResponse implements Serializable {

    /**
     * 已授权限
     */
    private List<String> grantedAuthority;

    /**
     * 部门数据权限
     */
    private List<Long> deptScopes;

}


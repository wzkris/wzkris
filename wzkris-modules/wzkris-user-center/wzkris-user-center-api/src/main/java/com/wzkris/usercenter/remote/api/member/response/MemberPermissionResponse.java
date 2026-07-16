package com.wzkris.usercenter.remote.api.member.response;

import com.wzkris.common.core.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberPermissionResponse implements Serializable {

    /**
     * 租户管理员
     */
    private boolean admin;

    /**
     * 已授权限
     */
    private List<String> grantedAuthority;

    /**
     * 用户角色列表（租户体系为岗位）
     */
    private List<UserRole> roles;

    public boolean getAdmin() {
        return this.admin;
    }

}

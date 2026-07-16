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
     * 用户角色列表（租户体系为岗位）
     */
    private List<UserRole> roles;

}

package com.wzkris.principal.feign.member.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberPermissionResp implements Serializable {

    /**
     * 租户管理员
     */
    private boolean admin;

    /**
     * 已授权限
     */
    private List<String> grantedAuthority;

    public boolean getAdmin() {
        return this.admin;
    }

}

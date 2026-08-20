package com.wzkris.usercenter.remote.api.admin.response;

import com.wzkris.common.core.model.UserRole;
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
     * 用户角色列表（携带数据权限信息）
     */
    private List<UserRole> roles;

}

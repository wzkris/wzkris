package com.thingslink.auth.domain.dto;


import com.thingslink.auth.domain.SysRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 角色传输层
 * @date : 2023/8/12 15:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleDTO extends SysRole {

    @Schema(description = "菜单组")
    private List<Long> menuIds;

    @Schema(description = "部门组")
    private List<Long> deptIds;
}

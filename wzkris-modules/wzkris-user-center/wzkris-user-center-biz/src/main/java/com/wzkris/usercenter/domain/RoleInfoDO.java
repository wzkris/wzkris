package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.usercenter.enums.role.DataScopeEnum;
import com.wzkris.usercenter.enums.role.RoleStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色表
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz", value = "role_info", autoResultMap = true)
public class RoleInfoDO extends BaseEntity {

    @TableId
    private Long roleId;

    @Schema(description = "数据范围")
    private DataScopeEnum dataScope;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "状态")
    private RoleStatusEnum status;

    @Schema(description = "角色排序")
    private Integer roleSort;

    public RoleInfoDO(Long roleId) {
        this.roleId = roleId;
    }

    public RoleInfoDO(RoleStatusEnum status) {
        this.status = status;
    }

}


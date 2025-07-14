package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色表 sys_role
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz")
public class SysRole extends BaseEntity {

    @TableId
    private Long roleId;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "数据范围（1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限）")
    private String dataScope;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "状态（0代表正常 1代表停用）")
    private String status;

    @Schema(description = "继承角色")
    private Boolean inherited;

    @Schema(description = "角色排序")
    private Integer roleSort;

    public SysRole(Long roleId) {
        this.roleId = roleId;
    }

    public SysRole(String status) {
        this.status = status;
    }

}

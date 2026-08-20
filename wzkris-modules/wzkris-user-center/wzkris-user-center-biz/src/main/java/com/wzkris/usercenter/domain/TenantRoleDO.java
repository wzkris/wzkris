package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseTenantEntity;
import com.wzkris.usercenter.enums.tenantrole.TenantRoleStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 租户角色表 tenant_role
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz", value = "tenant_role")
public class TenantRoleDO extends BaseTenantEntity {

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "状态（0代表正常 1代表停用）")
    private TenantRoleStatusEnum status;

    @Schema(description = "角色排序")
    private Integer roleSort;

    public TenantRoleDO(Long id) {
        this.setId(id);
    }

    public TenantRoleDO(TenantRoleStatusEnum status) {
        this.status = status;
    }

}


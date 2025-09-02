package com.wzkris.user.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 租户限制信息展示层
 *
 * @author wzkris
 */
@Data
public class TenantUsedQuotaVO {

    @Schema(description = "已有账号数量")
    private Integer accountHas;

    @Schema(description = "已有角色数量")
    private Integer roleHas;

    @Schema(description = "已有部门数量")
    private Integer deptHas;

}

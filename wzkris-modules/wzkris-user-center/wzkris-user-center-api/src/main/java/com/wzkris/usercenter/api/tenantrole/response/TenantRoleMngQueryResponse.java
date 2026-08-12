package com.wzkris.usercenter.api.tenantrole.response;

import com.wzkris.usercenter.enums.tenantrole.TenantRoleStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色详情响应（Mng 轨单对象 -> {域}MngQueryResponse）
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class TenantRoleMngQueryResponse {

    private Long id;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "状态（0代表正常 1代表停用）")
    private TenantRoleStatusEnum status;

    @Schema(description = "角色排序")
    private Integer roleSort;

}

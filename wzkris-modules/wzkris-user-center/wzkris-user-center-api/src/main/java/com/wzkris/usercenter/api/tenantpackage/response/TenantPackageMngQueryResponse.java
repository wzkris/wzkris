package com.wzkris.usercenter.api.tenantpackage.response;

import com.wzkris.usercenter.enums.tenantpackage.TenantPackageStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 租户套餐详情响应（Mng 轨单对象 -> {域}MngQueryResponse）
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class TenantPackageMngQueryResponse {

    private Long id;

    @Schema(description = "套餐名称")
    private String packageName;

    @Schema(description = "状态（0正常 1停用）")
    private TenantPackageStatusEnum status;

    @Schema(description = "套餐绑定的菜单")
    private Long[] menuIds;

    @Schema(description = "账号数量（-1不限制）")
    private Integer accountNumLimit;

    @Schema(description = "角色数量（-1不限制）")
    private Integer roleNumLimit;

    @Schema(description = "备注")
    private String remark;

}

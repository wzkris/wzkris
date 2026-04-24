package com.wzkris.usercenter.response.tenantpackage;

import com.wzkris.usercenter.enums.tenantpackage.TenantPackageStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TenantPackageMngResponse {

    private Long packageId;

    @Schema(description = "套餐名称")
    private String packageName;

    @Schema(description = "状态（0正常 1停用）")
    private TenantPackageStatusEnum status;

    @Schema(description = "套餐绑定的菜单")
    private Long[] menuIds;

    @Schema(description = "账号数量（-1不限制）")
    private Integer memberNumLimit;

    @Schema(description = "职位数量（-1不限制）")
    private Integer postNumLimit;

    @Schema(description = "备注")
    private String remark;

}

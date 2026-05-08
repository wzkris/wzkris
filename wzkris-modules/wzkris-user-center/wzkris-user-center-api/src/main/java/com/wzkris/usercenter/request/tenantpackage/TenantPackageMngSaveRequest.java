package com.wzkris.usercenter.request.tenantpackage;

import com.wzkris.usercenter.enums.tenantpackage.TenantPackageStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "新增租户套餐参数体")
public class TenantPackageMngSaveRequest {

    @NotBlank(message = "{invalidParameter.packageName.invalid}")
    @Size(min = 2, max = 20, message = "{invalidParameter.packageName.invalid}")
    @Schema(description = "套餐名称")
    private String packageName;

    @Schema(description = "状态（0 正常 1 停用）")
    private TenantPackageStatusEnum status;

    @Schema(description = "套餐绑定的菜单")
    private Long[] menuIds;

    @NotNull(message = "账号数量{validate.notnull}")
    @Schema(description = "账号数量（-1 不限制）")
    private Integer memberNumLimit;

    @NotNull(message = "职位数量{validate.notnull}")
    @Schema(description = "职位数量（-1 不限制）")
    private Integer postNumLimit;

    @Schema(description = "备注")
    private String remark;

}


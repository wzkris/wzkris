package com.wzkris.usercenter.request.tenantpackage;

import com.wzkris.common.core.constant.CommonConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 修改租户套餐请求体
 */
@Data
@Schema(description = "修改租户套餐参数体")
public class TenantPackageMngUpdateRequest {

    @NotNull(message = "{invalidParameter.id.invalid}")
    private Long packageId;

    @NotBlank(message = "{invalidParameter.packageName.invalid}")
    @Size(min = 2, max = 20, message = "{invalidParameter.packageName.invalid}")
    @Schema(description = "套餐名称")
    private String packageName;

    @Pattern(
            regexp = "[" +
                    CommonConstants.STATUS_ENABLE +
                    CommonConstants.STATUS_DISABLE
                    + "]",
            message = "{invalidParameter.status.invalid}")
    @Schema(description = "状态（0 正常 1 停用）")
    private String status;

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


package com.wzkris.principal.domain.req.tenantpackage;

import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.principal.domain.TenantPackageInfoDO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@AutoMappers({@AutoMapper(target = TenantPackageInfoDO.class)})
@Schema(description = "租户套餐管理添加修改参数体")
public class TenantPackageMngReq {

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
    @Schema(description = "状态（0正常 1停用）")
    private String status;

    @Schema(description = "套餐绑定的菜单")
    private List<Long> menuIds;

    @Schema(description = "备注")
    private String remark;

}

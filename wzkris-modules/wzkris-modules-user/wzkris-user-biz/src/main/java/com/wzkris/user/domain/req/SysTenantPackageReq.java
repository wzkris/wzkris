package com.wzkris.user.domain.req;

import com.wzkris.common.core.annotation.EnumsCheck;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.user.domain.SysTenantPackage;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@AutoMappers({@AutoMapper(target = SysTenantPackage.class)})
@Schema(description = "租户套餐添加修改参数体")
public class SysTenantPackageReq {

    private Long packageId;

    @NotBlank(message = "{desc.package}{desc.name}" + "{validate.notnull}")
    @Size(min = 2, max = 20, message = "{desc.package}{desc.name}" + "{validate.size.illegal}")
    @Schema(description = "套餐名称")
    private String packageName;

    @EnumsCheck(values = {CommonConstants.STATUS_ENABLE, CommonConstants.STATUS_DISABLE})
    @Schema(description = "状态（0正常 1停用）")
    private String status;

    @Schema(description = "套餐绑定的菜单")
    private List<Long> menuIds;

    @Schema(description = "菜单树选择项是否关联显示")
    private Boolean isMenuDisplay;

    @Schema(description = "备注")
    private String remark;
}

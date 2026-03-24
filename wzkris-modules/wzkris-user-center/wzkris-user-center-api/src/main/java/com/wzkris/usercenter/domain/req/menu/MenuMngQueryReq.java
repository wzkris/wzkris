package com.wzkris.usercenter.domain.req.menu;

import com.wzkris.common.validator.annotation.EnumsCheck;
import com.wzkris.usercenter.enums.MenuScopeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "菜单管理查询条件")
public class MenuMngQueryReq {

    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "0代表存在 1代表停用")
    private String status;

    @NotBlank(message = "{invalidParameter.menuScope.invalid}")
    @EnumsCheck(value = MenuScopeEnum.class, property = "value")
    @Schema(description = "菜单域")
    private String scope;

}

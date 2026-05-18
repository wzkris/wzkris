package com.wzkris.usercenter.request.menu;

import com.wzkris.usercenter.enums.menu.MenuScopeEnum;
import com.wzkris.usercenter.enums.menu.MenuStatusEnum;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "菜单管理查询条件")
public class MenuMngListRequest {

    @Parameter(description = "菜单名称")
    private String menuName;

    @Parameter(description = "0代表存在 1代表停用")
    private MenuStatusEnum status;

    @NotNull(message = "{invalidParameter.menuScope.invalid}")
    @Parameter(description = "菜单域")
    private MenuScopeEnum scope;

}

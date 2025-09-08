package com.wzkris.user.domain.req.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "菜单管理查询条件")
public class MenuManageQueryReq {

    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "0代表存在 1代表停用")
    private String status;

}

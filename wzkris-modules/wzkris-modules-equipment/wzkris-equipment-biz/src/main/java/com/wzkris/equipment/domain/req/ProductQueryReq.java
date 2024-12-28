package com.wzkris.equipment.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author wzkris
 */
@Data
@Schema(description = "筛选条件")
public class ProductQueryReq {

    @Schema(description = "产品名称")
    private String pdtName;

    @Schema(description = "产品类型 0 直连产品 1 网关产品")
    private String pdtType;
}

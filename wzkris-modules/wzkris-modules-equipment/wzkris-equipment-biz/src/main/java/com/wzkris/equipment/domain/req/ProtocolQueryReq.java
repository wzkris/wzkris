package com.wzkris.equipment.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author wzkris
 */
@Data
@Schema(description = "筛选条件")
public class ProtocolQueryReq {

    @Schema(description = "协议名称")
    private String ptcName;

    @Schema(description = "协议版本")
    private String ptcVersion;

    @Schema(description = "状态(字典值：0启用  1停用)")
    private String status;
}

package com.wzkris.equipment.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "站点查询条件")
public class StationQueryReq {

    @Schema(description = "站点名称")
    private String stationName;

    @Schema(description = "状态")
    private String status;
}

package com.wzkris.equipment.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author wzkris
 */
@Data
@Schema(description = "筛选条件")
public class DeviceQueryReq {

    @Schema(description = "设备名称")
    private String deviceName;

    @Schema(description = "站点ID")
    private Long stationId;

    @Schema(description = "产品ID")
    private Long pdtId;

    @Schema(description = "通讯标识")
    private String cmcid;

    @Schema(description = "连接状态")
    private Boolean online;

    @Schema(description = "状态")
    private String status;
}

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

    @Schema(description = "设备号")
    private String serialNo;

    @Schema(description = "连接状态")
    private Boolean online;

    @Schema(description = "状态")
    private String status;
}

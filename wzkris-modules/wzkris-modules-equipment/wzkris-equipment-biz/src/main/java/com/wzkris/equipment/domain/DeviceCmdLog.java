package com.wzkris.equipment.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * (DeviceCommandRecord)实体类
 *
 * @author wzkris
 * @since 2024-12-19 15:30:40
 */
@Data
@AllArgsConstructor
@Schema(description = "实体类: 设备指令日志")
public class DeviceCmdLog {

    @Schema(description = "设备ID")
    private Long deviceId;

    @Schema(description = "指令接收时间")
    private Long receiveTime;

    @Schema(description = "指令")
    private String cmd;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "指令数据")
    private String cmdData;

    @Schema(description = "指令类型（1=属性上报，2=功能调用，3=事件上报，4=设备升级，5=设备上线，6=设备离线）")
    private String cmdType;
}

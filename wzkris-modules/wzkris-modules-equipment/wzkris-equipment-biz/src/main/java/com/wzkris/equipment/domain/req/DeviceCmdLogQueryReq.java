package com.wzkris.equipment.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author wzkris
 */
@Data
@Schema(description = "筛选条件")
public class DeviceCmdLogQueryReq {

    @Schema(description = "设备ID")
    private Long deviceId;

    @Schema(description = "指令")
    private String cmd;

    @Schema(description = "指令类型（1=属性上报，2=功能调用，3=事件上报，4=设备升级，5=设备上线，6=设备离线）")
    private String cmdType;

    @Schema(description = "开始时间")
    private Long beginTime;

    @Schema(description = "结束时间")
    private Long endTime;

}

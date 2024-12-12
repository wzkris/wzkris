package com.wzkris.equipment.domain.vo;

import com.wzkris.equipment.domain.Device;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 设备展示层
 * @date : 2023/6/7 8:28
 */
@Data
@NoArgsConstructor
public class DeviceVO extends Device {

    @Serial
    private static final long serialVersionUID = -5565673176757994333L;

    @Schema(description = "产品名称")
    private String pdtName;

    @Schema(description = "协议名称")
    private String ptcName;

    @Schema(description = "电站名称")
    private String stationName;

    @Schema(description = "网络信息")
    private NetworkVO net;

}

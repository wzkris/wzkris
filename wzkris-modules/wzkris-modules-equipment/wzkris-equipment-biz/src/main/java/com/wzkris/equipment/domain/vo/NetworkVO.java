package com.wzkris.equipment.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class NetworkVO {

    @Schema(description = "入网IP")
    private String ip;

    @Schema(description = "入网端口")
    private Integer port;

    @Schema(description = "信号值")
    private Integer signal;

    @Schema(description = "温度")
    private Integer temperature;
}

package com.thingslink.equipment.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 站点展示层
 * @date : 2023/6/6 16:28
 */
@Data
@Accessors(chain = true)
public class StationVO {

    @Schema(description = "电站id")
    private Long stationId;

    @Schema(description = "电站名")
    private String stationName;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "经度")
    private BigDecimal longitude;

    @Schema(description = "纬度")
    private BigDecimal latitude;

    @Schema(description = "距离，单位米")
    private Long distance;
}

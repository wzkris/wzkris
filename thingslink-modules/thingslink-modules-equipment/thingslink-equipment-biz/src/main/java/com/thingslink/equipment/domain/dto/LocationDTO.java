package com.thingslink.equipment.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 位置信息
 * @date : 2023/6/6 14:42
 */
@Data
@Accessors
public class LocationDTO {

    @Schema(description = "经度")
    @DecimalMin(value = "-180.000000", message = "经度参数不正确")
    @DecimalMax(value = "180.000000", message = "经度参数不正确")
    private BigDecimal longitude;

    @Schema(description = "纬度")
    @DecimalMin(value = "-90.000000", message = "纬度参数不正确")
    @DecimalMax(value = "90.000000", message = "纬度参数不正确")
    private BigDecimal latitude;

    @Schema(description = "距离，单位米")
    @Min(value = 100, message = "筛选范围最小为100米")
    private Long distance;
}

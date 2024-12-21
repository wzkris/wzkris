package com.wzkris.equipment.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * (Station)实体类
 *
 * @author wzkris
 * @since 2024-12-09 12:56:40
 */
@Data
@NoArgsConstructor
@Schema(description = "站点信息")
public class Station extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -2977651045048928385L;

    @TableId
    private Long stationId;

    private Long tenantId;

    @NotNull(message = "[latitude] {validate.notnull}")
    @DecimalMin(value = "-90.000000", message = "纬度参数不正确")
    @DecimalMax(value = "90.000000", message = "纬度参数不正确")
    @Schema(description = "纬度")
    private BigDecimal latitude;

    @NotNull(message = "[longitude] {validate.notnull}")
    @DecimalMin(value = "-180.000000", message = "经度参数不正确")
    @DecimalMax(value = "180.000000", message = "经度参数不正确")
    @Schema(description = "经度")
    private BigDecimal longitude;

    @NotBlank(message = "[address] {validate.notnull}")
    @Schema(description = "地址信息")
    private String address;

    @NotBlank(message = "[stationName] {validate.notnull}")
    @Schema(description = "站点名称")
    private String stationName;

    @Schema(description = "站点状态")
    private String status;

    public Station(Long stationId) {
        this.stationId = stationId;
    }
}

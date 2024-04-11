package com.thingslink.equipment.domain;

import com.thingslink.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * (Station)实体类
 *
 * @author wzkris
 * @since 2023-06-05 16:39:06
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@FieldNameConstants
@EqualsAndHashCode(callSuper = true)
public class Station extends BaseEntity implements Serializable {

    @Schema(description = "电站id")
    private Long stationId;

    @Schema(description = "电站名")
    private String stationName;

    @Schema(description = "部门id")
    private Long deptId;

    @Schema(description = "省id")
    private Long provinceId;

    @Schema(description = "市id")
    private Long cityId;

    @Schema(description = "区/县id")
    private Long districtId;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "经度")
    private BigDecimal longitude;

    @Schema(description = "纬度")
    private BigDecimal latitude;
}

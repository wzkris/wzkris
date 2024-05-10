package com.thingslink.equipment.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.thingslink.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

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

    @TableId
    private Long stationId;

    @Schema(description = "电站名")
    private String stationName;

    @Schema(description = "租户id")
    private Long tenantId;

    @Schema(description = "部门id")
    private Long deptId;

    @Schema(description = "省/直辖市编码")
    private Integer provinceCode;

    @Schema(description = "市编码")
    private Integer cityCode;

    @Schema(description = "区/县编码")
    private Integer districtCode;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "经度")
    private BigDecimal longitude;

    @Schema(description = "纬度")
    private BigDecimal latitude;
}

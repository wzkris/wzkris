package com.wzkris.equipment.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;

/**
 * (Product)实体类
 *
 * @author wzkris
 * @since 2024-12-04 14:40:00
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class Product extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -7774898640593067868L;

    @TableId
    private Long pdtId;

    @Schema(description = "产品名称")
    private String pdtName;

    @Schema(description = "状态(字典值：0启用  1停用)")
    private String status;

    @Schema(description = "产品类型 0 直连产品 1 网关产品")
    private String pdtType;

    @Schema(description = "协议ID")
    private Long ptcId;

    @Schema(description = "产品型号类型")
    private String modelType;

    @Schema(description = "产品型号，包含一些不方便统一设计的字段")
    private Object model;

    @Schema(description = "产品描述")
    private String remark;

    public Product(Long pdtId) {
        this.pdtId = pdtId;
    }

    /**
     * 充电桩模型
     */
    @Data
    public static class ChargePileModel {
        public static final String type = "0";

        @Schema(description = "标准功率")
        private String stdPower;

        @Schema(description = "最大功率")
        private String maxPower;
    }

    /**
     * 监控模型
     */
    @Data
    public static class MonitorModel {
        public static final String type = "1";

        @Schema(description = "分辨率")
        private Integer resolution;
    }
}

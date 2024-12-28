package com.wzkris.equipment.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wzkris
 * @description 物模型
 * @since 2024-12-21
 */
@Data
@NoArgsConstructor
@Schema(description = "实体类: 物模型")
public class ThingsModel extends BaseEntity {

    @TableId
    private Long modelId;

    /**
     * 物模型名称
     */
    @Schema(description = "物模型名称")
    private String modelName;

    /**
     * 产品id
     */
    @Schema(description = "产品id")
    private Long pdtId;

    /**
     * 排序，值越大，排序越靠前
     */
    @Schema(description = "排序")
    private Integer modelSort;

    /**
     * 标识符，产品下唯一
     */
    @Schema(description = "标识符，产品下唯一")
    private String identifier;

    /**
     * 模型类别（1-属性，2-功能，3-事件）
     */
    @Schema(description = "模型类别（1-属性，2-功能，3-事件）")
    private String modelType;

    /**
     * 数据类型（integer、decimal、string、bool、array、enum）
     */
    @Schema(description = "数据类型（integer、decimal、string、bool、array、enum）")
    private String datatype;

    /**
     * 数据定义
     */
    @Schema(description = "数据定义")
    private String specs;
}

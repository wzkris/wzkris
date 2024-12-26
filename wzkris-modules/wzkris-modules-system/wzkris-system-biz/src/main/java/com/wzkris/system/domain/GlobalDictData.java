package com.wzkris.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 字典数据表 global_dict_data
 *
 * @author wzkris
 */
@Data
public class GlobalDictData extends BaseEntity {

    @TableId
    private Long dataId;

    @Schema(description = "排序")
    private Long dictSort;

    @Schema(description = "标签")
    private String dictLabel;

    @Schema(description = "键值")
    private String dictValue;

    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "样式属性（其他样式扩展）")
    private String cssClass;

    @Schema(description = "表格字典样式")
    private String listClass;

    @Schema(description = "是否默认（Y是 N否")
    private String isDefault;
}

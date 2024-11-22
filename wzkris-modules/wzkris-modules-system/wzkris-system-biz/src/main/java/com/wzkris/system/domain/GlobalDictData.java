package com.wzkris.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "字典标签不能为空")
    @Size(min = 1, max = 50, message = "字典标签长度不能超过100个字符")
    @Schema(description = "标签")
    private String dictLabel;

    @NotBlank(message = "字典键值不能为空")
    @Size(min = 1, max = 50, message = "字典键值长度不能超过100个字符")
    @Schema(description = "键值")
    private String dictValue;

    @NotBlank(message = "字典类型不能为空")
    @Size(min = 1, max = 50, message = "字典类型长度不能超过100个字符")
    @Schema(description = "字典类型")
    private String dictType;

    @Size(min = 0, max = 100, message = "样式属性长度不能超过100个字符")
    @Schema(description = "样式属性（其他样式扩展）")
    private String cssClass;

    @Schema(description = "表格字典样式")
    private String listClass;

    @Schema(description = "是否默认（Y是 N否")
    private String isDefault;
}

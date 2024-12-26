package com.wzkris.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 字典类型表 global_dict_type
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class GlobalDictType extends BaseEntity {

    @TableId
    private Long typeId;

    @Schema(description = "字典名称")
    private String dictName;

    @Schema(description = "字典类型")
    private String dictType;
}

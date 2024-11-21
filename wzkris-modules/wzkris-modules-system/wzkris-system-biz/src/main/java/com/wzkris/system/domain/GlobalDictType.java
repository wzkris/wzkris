package com.wzkris.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 字典类型表 global_dict_type
 *
 * @author wzkris
 */
@Data
@Accessors(chain = true)
public class GlobalDictType extends BaseEntity {

    @TableId
    private Long typeId;

    @NotBlank(message = "字典名称不能为空")
    @Size(min = 2, max = 50, message = "字典类型名称长度不能超过100个字符")
    @Schema(description = "字典名称")
    private String dictName;

    @NotBlank(message = "字典类型不能为空")
    @Size(min = 2, max = 50, message = "字典类型类型长度不能超过100个字符")
    @Pattern(regexp = "^[a-z][a-z0-9_]*$", message = "字典类型必须以字母开头，且只能为（小写字母，数字，下滑线）")
    @Schema(description = "字典类型")
    private String dictType;
}

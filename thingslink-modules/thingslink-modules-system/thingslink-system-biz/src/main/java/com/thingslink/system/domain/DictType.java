package com.thingslink.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.thingslink.common.orm.model.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;

/**
 * 字典类型表 dict_type
 *
 * @author wzkris
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class DictType extends BaseEntity {
    

    /**
     * 字典主键
     */
    @TableId
    private Long dictId;

    /**
     * 字典名称
     */
    @NotBlank(message = "字典名称不能为空")
    @Size(min = 0, max = 100, message = "字典类型名称长度不能超过100个字符")
    private String dictName;

    /**
     * 字典类型
     */
    @NotBlank(message = "字典类型不能为空")
    @Size(min = 0, max = 100, message = "字典类型类型长度不能超过100个字符")
    @Pattern(regexp = "^[a-z][a-z0-9_]*$", message = "字典类型必须以字母开头，且只能为（小写字母，数字，下滑线）")
    private String dictType;

    /**
     * 状态（0正常 1停用）
     */
    private String status;
}

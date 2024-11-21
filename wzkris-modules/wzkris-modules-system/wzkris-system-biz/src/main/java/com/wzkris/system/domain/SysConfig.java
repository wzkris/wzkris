package com.wzkris.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 参数配置表 config
 *
 * @author wzkris
 */
@Data
@Accessors(chain = true)
public class SysConfig extends BaseEntity {

    @TableId
    private Long configId;

    @NotBlank(message = "参数名称不能为空")
    @Size(min = 1, max = 50, message = "参数名称{validate.size.illegal}")
    @Schema(description = "参数名称")
    private String configName;

    @NotBlank(message = "参数键名长度不能为空")
    @Size(min = 3, max = 50, message = "参数键名{validate.size.illegal}")
    @Schema(description = "参数键名")
    private String configKey;

    @NotBlank(message = "参数键值不能为空")
    @Size(min = 1, max = 500, message = "参数键值{validate.size.illegal}")
    @Schema(description = "参数键值")
    private String configValue;

    @Schema(description = "系统内置（Y是 N否）")
    private String configType;
}

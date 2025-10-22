package com.wzkris.message.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 参数配置表 config_info
 *
 * @author wzkris
 */
@Data
@TableName(schema = "biz", value = "config_info")
public class ConfigInfoDO extends BaseEntity {

    @TableId
    private Long configId;

    @Schema(description = "参数名称")
    private String configName;

    @Schema(description = "参数键名")
    private String configKey;

    @Schema(description = "参数键值")
    private String configValue;

    @Schema(description = "配置类型")
    private String configType;

    @Schema(description = "是否内置")
    private Boolean builtIn;

}

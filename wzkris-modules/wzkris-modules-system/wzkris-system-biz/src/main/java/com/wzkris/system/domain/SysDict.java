package com.wzkris.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 字典表
 *
 * @author wzkris
 */
@Data
@TableName(schema = "biz", autoResultMap = true)
public class SysDict extends BaseEntity {

    @TableId
    private Long dictId;

    @Schema(description = "字典键")
    private String dictKey;

    @Schema(description = "字典名称")
    private String dictName;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "字典键值")
    private DictData[] dictValue;

    @Schema(description = "备注")
    private String remark;

    @Data
    public static class DictData {

        @Schema(description = "字典值")
        private String value;

        @Schema(description = "字典标签")
        private String label;

        @Schema(description = "表格回显样式")
        private String tableCls;

    }

}

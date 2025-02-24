package com.wzkris.equipment.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 协议
 *
 * @author wzkris
 * @since 2024-11-19 16:31:40
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz_sys")
public class Protocol extends BaseEntity {

    @TableId
    private Long ptcId;

    @Schema(description = "协议名称")
    private String ptcName;

    @Schema(description = "协议版本")
    private String ptcVersion;

    @Schema(description = "协议类型 ：mqtt || coap || modbus || http")
    private String ptcType;

    @Schema(description = "协议语言")
    private String ptcLanguage;

    @Schema(description = "类名")
    private String className;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "示例参数")
    private String parameter;

    @Schema(description = "状态(字典值：0启用  1停用)")
    private String status;

    @Schema(description = "备注")
    private String remark;

    public Protocol(Long ptcId) {
        this.ptcId = ptcId;
    }
}
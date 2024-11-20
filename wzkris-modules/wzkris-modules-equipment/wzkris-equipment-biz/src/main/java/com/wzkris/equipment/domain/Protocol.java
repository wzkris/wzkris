package com.wzkris.equipment.domain;

import com.baomidou.mybatisplus.annotation.TableId;
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
public class Protocol extends BaseEntity {
    /**
     * id
     */
    @TableId
    private Long ptcId;

    /**
     * 产品标识
     */
    @Schema(description = "产品标识")
    private String productKey;

    /**
     * 协议名称
     */
    @Schema(description = "协议名称")
    private String ptcName;

    /**
     * 协议标识
     */
    @Schema(description = "协议标识")
    private String ptcKey;

    /**
     * 协议版本
     */
    @Schema(description = "协议版本")
    private String ptcVersion;

    /**
     * 协议类型 ：mqtt || coap || modbus || http
     */
    @Schema(description = "协议类型 ：mqtt || coap || modbus || http")
    private String ptcType;

    /**
     * 协议语言
     */
    @Schema(description = "协议语言")
    private String ptcLanguage;

    /**
     * 类名
     */
    @Schema(description = "类名")
    private String className;

    /**
     * 内容
     */
    @Schema(description = "内容")
    private String content;

    /**
     * 状态(字典值：0启用  1停用)
     */
    @Schema(description = "状态(字典值：0启用  1停用)")
    private String status;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    public Protocol(Long ptcId) {
        this.ptcId = ptcId;
    }
}
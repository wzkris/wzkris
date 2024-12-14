package com.wzkris.user.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wzkris.common.core.annotation.Sensitive;
import com.wzkris.common.core.annotation.impl.SensitiveStrategy;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 租户信息展示层
 *
 * @author wzkris
 */
@Data
@JsonInclude(JsonInclude.Include.NON_DEFAULT)// 默认值不序列化
public class SysTenantOwnVO {

    @Schema(description = "是否超级租户")
    private boolean supert;

    @Sensitive(strategy = SensitiveStrategy.ID_CARD)
    @Schema(description = "超级管理员账号")
    private String administrator;

    @Schema(description = "租户类型 0-个人 1-企业")
    private String tenantType;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "租户名称")
    private String tenantName;

    @Schema(description = "租户状态")
    private String status;

    @Schema(description = "域名")
    private String domain;

    @Schema(description = "过期时间（-1不限制）")
    private Long expireTime;

    @Schema(description = "账号数量（-1不限制）")
    private Integer accountLimit;

    @Schema(description = "角色数量（-1不限制）")
    private Integer roleLimit;

    @Schema(description = "岗位数量（-1不限制）")
    private Integer postLimit;

    @Schema(description = "部门数量（-1不限制）")
    private Integer deptLimit;

    public SysTenantOwnVO() {
        this.supert = false;
    }

    public SysTenantOwnVO(boolean supert) {
        this.supert = supert;
    }
}
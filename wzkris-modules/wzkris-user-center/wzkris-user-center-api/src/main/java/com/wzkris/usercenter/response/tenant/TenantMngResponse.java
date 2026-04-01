package com.wzkris.usercenter.response.tenant;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 租户管理展示层
 *
 * @author wzkris
 */
@Data
public class TenantMngResponse {

    private Long tenantId;

    @Schema(description = "管理员ID")
    private Long administrator;

    @Schema(description = "租户类型 0-个人 1-企业")
    private String tenantType;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "租户名称")
    private String tenantName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "操作密码")
    private String operPwd;

    @Schema(description = "租户状态")
    private String status;

    @Schema(description = "域名")
    private String domain;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "租户套餐编号")
    private Long packageId;

    @Schema(description = "过期时间")
    private Date expireTime;

    @Schema(description = "账号数量（-1不限制）")
    private Integer accountLimit;

    @Schema(description = "职位数量（-1不限制）")
    private Integer postLimit;

    @Schema(description = "套餐名称")
    private String packageName;

    @Schema(description = "余额, 元")
    private BigDecimal balance;

}


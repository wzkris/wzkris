package com.wzkris.usercenter.request.tenant;

import com.wzkris.common.validator.annotation.Xss;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

/**
 * 新增租户请求体
 */
@Data
@Schema(description = "新增租户参数体")
public class TenantMngSaveRequest {

    @Pattern(regexp = "[01]", message = "{invalidParameter.tenantType.invalid}")
    @Schema(description = "租户类型 0-个人 1-企业")
    private String tenantType;

    @Schema(description = "联系电话")
    private String contactPhone;

    @NotBlank(message = "{invalidParameter.tenantName.invalid}")
    @Schema(description = "租户名称")
    private String tenantName;

    @Schema(description = "租户状态")
    private String status;

    @Schema(description = "域名")
    private String domain;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "租户套餐编号")
    private Long packageId;

    @NotNull(message = "{invalidParameter.expireTime.invalid}")
    @Future(message = "{invalidParameter.expireTime.invalid}")
    @Schema(description = "过期时间")
    private Date expireTime;

    @NotNull(message = "账号数量{validate.notnull}")
    @Schema(description = "账号数量（-1 不限制）")
    private Integer accountLimit;

    @NotNull(message = "职位数量{validate.notnull}")
    @Schema(description = "角色数量（-1 不限制）")
    private Integer postLimit;

    // 用户名只能为小写英文、数字和下划线
    @Pattern(regexp = "^[a-z0-9_]+$", message = "{invalidParameter.username.invalid}")
    @Xss
    @NotBlank(message = "{invalidParameter.username.invalid}")
    @Schema(description = "登录用户名")
    private String username;

}


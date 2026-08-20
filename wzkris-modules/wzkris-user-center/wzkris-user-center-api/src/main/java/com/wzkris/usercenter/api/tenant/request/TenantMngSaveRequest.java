package com.wzkris.usercenter.api.tenant.request;

import com.wzkris.common.validator.annotation.Xss;
import com.wzkris.usercenter.enums.tenant.TenantStatusEnum;
import com.wzkris.usercenter.enums.tenant.TenantTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Schema(description = "新增租户参数体")
public class TenantMngSaveRequest {

    @NotNull(message = "{invalidParameter.param.invalid}")
    @Schema(description = "租户类型")
    private TenantTypeEnum tenantType;

    @Schema(description = "联系电话")
    private String contactPhone;

    @NotBlank(message = "{invalidParameter.tenantName.invalid}")
    @Schema(description = "租户名称")
    private String tenantName;

    @NotNull(message = "{invalidParameter.status.invalid}")
    @Schema(description = "租户状态")
    private TenantStatusEnum status;

    @Schema(description = "域名")
    private String domain;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "租户套餐编号")
    private Long packageId;

    @NotNull(message = "{invalidParameter.expireTime.invalid}")
    @Future(message = "{invalidParameter.expireTime.invalid}")
    @Schema(description = "过期时间")
    private OffsetDateTime expireTime;

    // 用户名只能为小写英文、数字和下划线
    @Pattern(regexp = "^[a-z0-9_]+$", message = "{invalidParameter.username.invalid}")
    @Xss
    @NotBlank(message = "{invalidParameter.username.invalid}")
    @Schema(description = "登录用户名")
    private String username;

}


package com.wzkris.usercenter.request.tenant;

import com.wzkris.usercenter.enums.tenant.TenantStatusEnum;
import com.wzkris.usercenter.enums.tenant.TenantTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * 修改租户请求体
 */
@Data
@Schema(description = "修改租户参数体")
public class TenantMngUpdateRequest {

    @NotNull(message = "{invalidParameter.id.invalid}")
    private Long tenantId;

    @Schema(description = "租户类型")
    private TenantTypeEnum tenantType;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "租户名称")
    private String tenantName;

    @Schema(description = "租户状态")
    private TenantStatusEnum status;

    @Schema(description = "域名")
    private String domain;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "租户套餐编号")
    private Long packageId;

    @Schema(description = "过期时间")
    private OffsetDateTime expireTime;

}


package com.wzkris.usercenter.api.tenant.response;

import com.wzkris.usercenter.enums.tenant.TenantStatusEnum;
import com.wzkris.usercenter.enums.tenant.TenantTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * 租户信息展示层
 *
 * @author wzkris
 */
@Data
public class TenantInfoResponse {

    @Schema(description = "租户名称")
    private String tenantName;

    @Schema(description = "租户类型")
    private TenantTypeEnum tenantType;

    @Schema(description = "套餐名称")
    private String packageName;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "租户状态")
    private TenantStatusEnum status;

    @Schema(description = "域名")
    private String domain;

    @Schema(description = "过期时间")
    private OffsetDateTime expireTime;

}


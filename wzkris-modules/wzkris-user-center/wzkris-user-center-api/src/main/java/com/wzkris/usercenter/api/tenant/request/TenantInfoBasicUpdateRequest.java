package com.wzkris.usercenter.api.tenant.request;

import com.wzkris.common.validator.annotation.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@Schema(description = "租户信息修改参数体")
public class TenantInfoBasicUpdateRequest {

    @PhoneNumber
    @Schema(description = "联系电话")
    private String contactPhone;

    @Size(min = 2, max = 20, message = "{invalidParameter.tenantName.invalid}")
    @Schema(description = "租户名称")
    private String tenantName;

    @URL(protocol = "https")
    @Schema(description = "域名")
    private String domain;

}


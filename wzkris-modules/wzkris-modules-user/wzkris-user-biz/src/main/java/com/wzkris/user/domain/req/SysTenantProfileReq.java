package com.wzkris.user.domain.req;

import com.wzkris.common.core.annotation.PhoneNumber;
import com.wzkris.user.domain.SysTenant;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@AutoMappers({@AutoMapper(target = SysTenant.class)})
@Schema(description = "租户信息修改参数体")
public class SysTenantProfileReq {

    @PhoneNumber
    @Schema(description = "联系电话")
    private String contactPhone;

    @Size(min = 2, max = 20, message = "{desc.tenant}{desc.name}" + "{validate.size.illegal}")
    @Schema(description = "租户名称")
    private String tenantName;

    @URL(protocol = "https")
    @Schema(description = "域名")
    private String domain;
}

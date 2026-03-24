package com.wzkris.usercenter.domain.req.tenant;

import com.wzkris.usercenter.domain.TenantInfoDO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

/**
 * 修改租户请求体
 */
@Data
@AutoMappers({@AutoMapper(target = TenantInfoDO.class)})
@Schema(description = "修改租户参数体")
public class TenantMngEditReq {

    @NotNull(message = "{invalidParameter.id.invalid}")
    private Long tenantId;

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

}

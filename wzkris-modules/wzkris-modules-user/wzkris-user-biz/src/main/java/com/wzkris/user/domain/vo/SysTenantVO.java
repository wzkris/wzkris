package com.wzkris.user.domain.vo;

import com.wzkris.user.domain.SysTenant;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 租户信息展示层
 *
 * @author wzkris
 */
@Data
@AutoMappers(
        @AutoMapper(target = SysTenant.class)
)
@NoArgsConstructor
public class SysTenantVO {

    @Schema(description = "是否超级租户")
    private boolean supert;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "企业名称")
    private String companyName;

    @Schema(description = "统一社会信用代码")
    private String licenseNumber;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "企业简介")
    private String intro;

    @Schema(description = "租户状态")
    private String status;

    public SysTenantVO(boolean supert) {
        this.supert = supert;
    }
}

package com.wzkris.usercenter.domain.req.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "用户增长请求")
public class CustomerIncryQueryReq {

    @NotBlank(message = "需要参数日期类型")
    @Schema(description = "日期类型")
    private String dateType;

    @NotNull(message = "需要参数开始日期")
    @Schema(description = "开始日期")
    private Date beginTime;

    @NotNull(message = "需要参数结束日期")
    @Schema(description = "结束日期")
    private Date endTime;

}

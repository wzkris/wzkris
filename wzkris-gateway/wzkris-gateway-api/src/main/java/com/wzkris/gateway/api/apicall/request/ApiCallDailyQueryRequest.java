package com.wzkris.gateway.api.apicall.request;

import com.wzkris.common.core.enums.AuthTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Schema(description = "API调用日统计查询参数")
public class ApiCallDailyQueryRequest {

    @Schema(description = "认证类型")
    private AuthTypeEnum authType;

    @Schema(description = "日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

}

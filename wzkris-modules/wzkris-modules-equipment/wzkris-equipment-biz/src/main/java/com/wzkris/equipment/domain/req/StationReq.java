package com.wzkris.equipment.domain.req;

import com.wzkris.common.core.annotation.EnumsCheck;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.equipment.domain.Station;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AutoMappers({@AutoMapper(target = Station.class)})
@Schema(description = "站点添加修改参数体")
public class StationReq {

    private Long stationId;

    @NotNull(message = "{desc.latitude}" + "{validate.notnull}")
    @DecimalMin(value = "-90.000000", message = "{desc.latitude}{validate.illegal}")
    @DecimalMax(value = "90.000000", message = "{desc.latitude}{validate.illegal}")
    @Schema(description = "纬度")
    private BigDecimal latitude;

    @NotNull(message = "{desc.longitude}" + "{validate.notnull}")
    @DecimalMin(value = "-90.000000", message = "{desc.longitude}{validate.illegal}")
    @DecimalMax(value = "90.000000", message = "{desc.longitude}{validate.illegal}")
    @Schema(description = "经度")
    private BigDecimal longitude;

    @NotBlank(message = "{desc.address}" + "{validate.notnull}")
    @Size(min = 2, max = 128, message = "{desc.address}" + "{validate.size.illegal}")
    @Schema(description = "地址信息")
    private String address;

    @NotBlank(message = "{desc.station}{desc.name}" + "{validate.notnull}")
    @Size(min = 2, max = 32, message = "{desc.station}{desc.name}" + "{validate.size.illegal}")
    @Schema(description = "站点名称")
    private String stationName;

    @EnumsCheck(values = {CommonConstants.STATUS_DISABLE, CommonConstants.STATUS_ENABLE})
    @Schema(description = "站点状态")
    private String status;

}

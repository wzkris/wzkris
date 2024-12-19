package com.wzkris.equipment.domain.req;

import com.wzkris.common.core.annotation.group.ValidationGroups;
import com.wzkris.common.security.field.annotation.FieldPerms;
import com.wzkris.common.security.field.enums.FieldPerm;
import com.wzkris.equipment.domain.Device;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AutoMappers({@AutoMapper(target = Device.class)})
@Schema(description = "设备添加修改参数体")
public class DeviceReq {

    private Long deviceId;

    @FieldPerms(perm = FieldPerm.WRITE, value = "@ps.hasPerms('device:field')")
    @NotNull(message = "[pdtId] {validate.notnull}", groups = ValidationGroups.Insert.class)
    @Schema(description = "产品ID")
    private Long pdtId;

    @Schema(description = "站点ID")
    private Long stationId;

    @Schema(description = "设备名称")
    private String deviceName;

    @FieldPerms(perm = FieldPerm.WRITE, value = "@ps.hasPerms('device:field')")
    @NotBlank(message = "[cmcid] {validate.notnull}", groups = ValidationGroups.Insert.class)
    @Schema(description = "通信标识")
    private String cmcid;

    @DecimalMin(value = "-90.000000", message = "纬度参数不正确")
    @DecimalMax(value = "90.000000", message = "纬度参数不正确")
    @Schema(description = "纬度")
    private BigDecimal latitude;

    @DecimalMin(value = "-180.000000", message = "经度参数不正确")
    @DecimalMax(value = "180.000000", message = "经度参数不正确")
    @Schema(description = "经度")
    private BigDecimal longitude;
}

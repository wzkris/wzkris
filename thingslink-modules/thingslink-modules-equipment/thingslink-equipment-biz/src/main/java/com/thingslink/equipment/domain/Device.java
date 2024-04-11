package com.thingslink.equipment.domain;

import com.thingslink.common.orm.model.BaseEntity;
import com.thingslink.equipment.enums.DeviceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * (Device)实体类
 *
 * @author wzkris
 * @since 2023-08-21 09:34:40
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@FieldNameConstants
@EqualsAndHashCode(callSuper = true)
public class Device extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 253601802815121784L;

    @Schema(description = "设备id")
    private Long deviceId;

    @Schema(description = "设备名称")
    private String deviceName;

    @Schema(description = "电站id")
    private Long stationId;

    @Schema(description = "sn号")
    private String serialNo;

    @Schema(description = "协议版本")
    private String version;

    @Schema(description = "在线时间")
    private LocalDateTime onlineTime;

    /**
     * {@link DeviceStatus}
     */
    @Schema(description = "状态")
    private String deviceStatus;

    @Schema(description = "通道状态 0-空闲 1-使用")
    private String roadStatus;

    public Device(Long deviceId) {
        this.deviceId = deviceId;
    }
}

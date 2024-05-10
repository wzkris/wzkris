package com.thingslink.equipment.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.thingslink.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.io.Serial;

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

    @TableId
    private Long deviceId;

    @Schema(description = "设备名称")
    private String deviceName;

    @Schema(description = "租户id")
    private Long tenantId;

    @Schema(description = "电站id")
    private Long stationId;

    @Schema(description = "设备号")
    private String serialNo;

    @Schema(description = "协议版本")
    private String version;

    @Schema(description = "上线时间")
    private Long onlineTime;

    @Schema(description = "下线时间")
    private Long offlineTime;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "通道状态 0-空闲 1-使用")
    private String roadStatus;

    public Device(Long deviceId) {
        this.deviceId = deviceId;
    }
}

package com.wzkris.equipment.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * (Device)实体类
 *
 * @author wzkris
 * @since 2023-08-21 09:34:40
 */
@Data
@NoArgsConstructor
@Schema(description = "实体类: 设备信息")
public class Device extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 253601802815121784L;

    @TableId
    private Long deviceId;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "产品ID")
    private Long pdtId;

    @Schema(description = "站点ID")
    private Long stationId;

    @Schema(description = "设备名称")
    private String deviceName;

    @Schema(description = "通信标识")
    private String cmcid;

    @Schema(description = "纬度")
    private BigDecimal latitude;

    @Schema(description = "经度")
    private BigDecimal longitude;

    @Schema(description = "在线离线")
    private Boolean online;

    @Schema(description = "设备状态")
    private String status;

    @Schema(description = "告警状态")
    private String alarm;

    @Schema(description = "上线时间")
    private Long onlineTime;

    @Schema(description = "下线时间")
    private Long offlineTime;

    public Device(Long deviceId) {
        this.deviceId = deviceId;
    }
}

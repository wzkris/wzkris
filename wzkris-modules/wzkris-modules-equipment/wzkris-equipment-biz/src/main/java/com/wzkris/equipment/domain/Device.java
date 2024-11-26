package com.wzkris.equipment.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
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
public class Device extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 253601802815121784L;

    @TableId
    private Long deviceId;

    @Schema(description = "租户id")
    private Long tenantId;

    @Schema(description = "设备名称")
    private String deviceName;

    @NotBlank(message = "[serialNo] {validate.notnull}")
    @Schema(description = "设备号")
    private String serialNo;

    @Schema(description = "上线时间")
    private Long onlineTime;

    @Schema(description = "下线时间")
    private Long offlineTime;

    @Schema(description = "连接状态")
    private String connStatus;

    @Schema(description = "状态")
    private String status;

    public Device(Long deviceId) {
        this.deviceId = deviceId;
    }
}

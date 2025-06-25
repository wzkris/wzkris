package com.wzkris.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "未读统计")
public class UnreadVO {

    @Schema(description = "系统通知未读")
    private int system;

    @Schema(description = "设备通知未读")
    private int device;

    public UnreadVO(int system, int device) {
        this.system = system;
        this.device = device;
    }

}

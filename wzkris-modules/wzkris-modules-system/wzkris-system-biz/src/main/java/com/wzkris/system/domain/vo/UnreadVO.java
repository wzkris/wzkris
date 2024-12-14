package com.wzkris.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "未读统计")
public class UnreadVO {

    @Schema(description = "通知未读数量")
    private int notify;

    public UnreadVO(int notify) {
        this.notify = notify;
    }
}

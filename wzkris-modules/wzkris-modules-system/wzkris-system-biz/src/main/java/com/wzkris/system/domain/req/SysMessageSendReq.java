package com.wzkris.system.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

@Data
@Schema(description = "筛选条件")
public class SysMessageSendReq {

    @Schema(description = "接收用户ID")
    private List<Long> userIds;

    @Schema(description = "通知ID")
    private Long msgId;
}

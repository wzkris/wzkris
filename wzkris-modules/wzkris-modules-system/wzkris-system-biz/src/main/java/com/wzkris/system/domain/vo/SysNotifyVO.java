package com.wzkris.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "通知信息")
public class SysNotifyVO extends SysAnnouncementVO {

    @Schema(description = "已读未读")
    private String readState;
}

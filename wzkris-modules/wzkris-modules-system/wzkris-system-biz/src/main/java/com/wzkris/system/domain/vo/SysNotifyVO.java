package com.wzkris.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "通知信息")
public class SysNotifyVO {

    private Long notifyId;

    private Long userId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "已读未读")
    private String readState;

    @Schema(description = "创建时间")
    private Long createAt;
}

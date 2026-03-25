package com.wzkris.system.response.announcement;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AnnouncementMngResponse {

    private Long announcementId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "状态（0草稿 1关闭 2公开）")
    private String status;

}

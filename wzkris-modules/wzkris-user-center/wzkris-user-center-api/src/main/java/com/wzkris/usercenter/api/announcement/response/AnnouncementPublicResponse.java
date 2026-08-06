package com.wzkris.usercenter.api.announcement.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Schema(description = "公告信息")
public class AnnouncementPublicResponse {

    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "创建时间")
    private OffsetDateTime createAt;

}

package com.wzkris.system.domain.vo.announcement;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "公告信息")
public class AnnouncementInfoVO {

    private Long announcementId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "创建时间")
    private Date createAt;

}

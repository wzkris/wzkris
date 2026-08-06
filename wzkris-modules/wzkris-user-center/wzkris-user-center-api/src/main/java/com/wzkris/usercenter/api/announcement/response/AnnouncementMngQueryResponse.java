package com.wzkris.usercenter.api.announcement.response;

import com.wzkris.usercenter.enums.announcement.AnnouncementStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公告详情响应（Mng 轨单对象 -> {域}MngQueryResponse）
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class AnnouncementMngQueryResponse {

    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "状态（0草稿 1关闭 2公开）")
    private AnnouncementStatusEnum status;

}

package com.wzkris.message.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公告表
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz", value = "announcement_info")
public class AnnouncementInfoDO extends BaseEntity {

    @TableId
    private Long announcementId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "状态（0草稿 1关闭 2公开）")
    private String status;

}

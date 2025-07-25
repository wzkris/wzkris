package com.wzkris.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 系统通知
 *
 * @author wzkris
 */
@Data
@TableName(schema = "biz")
public class SysNotice {

    @TableId
    private Long noticeId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "通知类型（0系统通知 1设备告警）")
    private String noticeType;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "创建者ID")
    private Long creatorId;

    @Schema(description = "创建时间")
    private Date createAt;

    public SysNotice() {
        this.createAt = new Date();
    }

}

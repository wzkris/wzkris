package com.wzkris.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 系统消息发送表
 *
 * @author wzkris
 */
@Data
public class SysNotify {

    @TableId
    private Long notifyId;

    @Schema(description = "通知类型（0系统通知 1设备告警）")
    private String notifyType;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "创建时间")
    private Long createAt;

    public SysNotify() {
        this.createAt = System.currentTimeMillis();
    }
}

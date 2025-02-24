package com.wzkris.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统消息表
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz_sys")
public class SysMessage extends BaseEntity {

    @TableId
    private Long msgId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "消息类型（1系统公告 2APP公告）")
    private String msgType;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "状态（0草稿 1关闭 2已发送）")
    private String status;

    public SysMessage(Long msgId) {
        this.msgId = msgId;
    }
}

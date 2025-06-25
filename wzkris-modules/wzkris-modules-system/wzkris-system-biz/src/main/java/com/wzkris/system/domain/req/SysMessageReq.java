package com.wzkris.system.domain.req;

import com.wzkris.common.core.annotation.EnumsCheck;
import com.wzkris.common.core.annotation.Xss;
import com.wzkris.system.constant.MessageConstants;
import com.wzkris.system.domain.SysMessage;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@AutoMappers({@AutoMapper(target = SysMessage.class)})
@Schema(description = "系统消息添加修改参数体")
public class SysMessageReq {

    private Long msgId;

    @Xss(message = "{desc.system}{desc.message}" + "{validate.xss.forbid}")
    @NotBlank(message = "{desc.system}{desc.message}" + "{validate.notnull}")
    @Size(min = 2, max = 30, message = "{desc.system}{desc.message}" + "{validate.size.illegal}")
    @Schema(description = "标题")
    private String title;

    @EnumsCheck(values = {MessageConstants.TYPE_APP, MessageConstants.TYPE_SYSTEM})
    @Schema(description = "消息类型（1系统公告 2APP公告）")
    private String msgType;

    @Schema(description = "内容")
    private String content;

    @EnumsCheck(
            values = {MessageConstants.STATUS_CLOSED, MessageConstants.STATUS_DRAFT, MessageConstants.STATUS_PUBLISH})
    @Schema(description = "状态（0草稿 1关闭 2已发送）")
    private String status;

}

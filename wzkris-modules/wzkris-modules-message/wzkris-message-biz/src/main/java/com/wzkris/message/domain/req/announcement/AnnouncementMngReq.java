package com.wzkris.message.domain.req.announcement;

import com.wzkris.common.validator.annotation.EnumsCheck;
import com.wzkris.common.validator.annotation.Xss;
import com.wzkris.message.domain.AnnouncementInfoDO;
import com.wzkris.message.enums.AnncStatusEnum;
import com.wzkris.message.enums.AnncTypeEnum;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@AutoMappers({@AutoMapper(target = AnnouncementInfoDO.class)})
@Schema(description = "系统消息添加修改参数体")
public class AnnouncementMngReq {

    private Long announcementId;

    @Xss
    @NotBlank(message = "{invalidParameter.messageTitle.invalid}")
    @Size(min = 2, max = 30, message = "{invalidParameter.messageTitle.invalid}")
    @Schema(description = "标题")
    private String title;

    @EnumsCheck(value = AnncTypeEnum.class, property = "value")
    @Schema(description = "消息类型（1系统公告 2APP公告）")
    private String msgType;

    @Schema(description = "内容")
    private String content;

    @EnumsCheck(value = AnncStatusEnum.class, property = "value")
    @Schema(description = "状态（0草稿 1关闭 2公开）")
    private String status;

}

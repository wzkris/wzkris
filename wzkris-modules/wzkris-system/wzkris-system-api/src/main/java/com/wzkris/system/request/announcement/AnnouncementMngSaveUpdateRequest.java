package com.wzkris.system.request.announcement;

import com.wzkris.common.validator.annotation.Xss;
import com.wzkris.system.enums.announcement.AnnouncementStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "系统消息添加修改参数体")
public class AnnouncementMngSaveUpdateRequest {

    private Long announcementId;

    @Xss
    @NotBlank(message = "{invalidParameter.messageTitle.invalid}")
    @Size(min = 2, max = 30, message = "{invalidParameter.messageTitle.invalid}")
    @Schema(description = "标题")
    private String title;

    @Schema(description = "消息类型")
    private String msgType;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "状态")
    private AnnouncementStatusEnum status;

}


package com.wzkris.system.domain.vo;

import com.wzkris.common.core.annotation.Xss;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "公告信息")
public class SysAnnouncementVO {

    private Long msgId;

    @Xss(message = "标题不能包含脚本字符")
    @NotBlank(message = "标题不能为空")
    @Size(min = 2, max = 30, message = "消息标题{validate.size.illegal}")
    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "创建时间")
    private Long createAt;
}

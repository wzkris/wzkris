package com.wzkris.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "简易消息体")
public class SimpleMessageDTO {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "消息类型")
    private String type;

    @Schema(description = "内容")
    private String content;
}

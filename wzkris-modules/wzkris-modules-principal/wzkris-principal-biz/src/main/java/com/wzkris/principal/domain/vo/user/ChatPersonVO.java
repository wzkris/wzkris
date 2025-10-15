package com.wzkris.principal.domain.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "聊天对象")
public class ChatPersonVO {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名称")
    private String name;

    @Schema(description = "头像")
    private String avatar;

}

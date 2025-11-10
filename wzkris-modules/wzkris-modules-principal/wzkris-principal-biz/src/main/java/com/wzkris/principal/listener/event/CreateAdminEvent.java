package com.wzkris.principal.listener.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建管理员事件
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdminEvent {

    @Schema(description = "接收方ID")
    private Long receiverId;

    private String username;

    private String password;

}

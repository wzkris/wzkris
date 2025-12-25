package com.wzkris.usercenter.listener.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建租户账号事件
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMemberEvent {

    @Schema(description = "接收方ID")
    private Long receiverId;

    private String username;

    private String password;

}

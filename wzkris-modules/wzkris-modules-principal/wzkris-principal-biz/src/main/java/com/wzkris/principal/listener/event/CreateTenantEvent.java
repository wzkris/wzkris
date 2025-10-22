package com.wzkris.principal.listener.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建租户事件
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTenantEvent {

    @Schema(description = "接收方ID")
    private Long toUserId;

    private String username;

    private String tenantName;

    private String loginPwd;

    private String operPwd;

}

package com.thingslink.user.api.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登录信息
 *
 * @author wzkris
 */
@Data
public class LoginInfoDTO {
    private Long userId;
    private String loginIp;
    private LocalDateTime loginDate;
}

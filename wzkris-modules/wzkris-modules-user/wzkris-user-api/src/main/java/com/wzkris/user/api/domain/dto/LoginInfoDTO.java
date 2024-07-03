package com.wzkris.user.api.domain.dto;

import lombok.Data;

/**
 * 登录信息
 *
 * @author wzkris
 */
@Data
public class LoginInfoDTO {
    private Long userId;
    private String loginIp;
    private Long loginDate;
}

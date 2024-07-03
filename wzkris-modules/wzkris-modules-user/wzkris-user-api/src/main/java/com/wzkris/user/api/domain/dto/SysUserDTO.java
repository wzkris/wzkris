package com.wzkris.user.api.domain.dto;

import lombok.Data;

/**
 * sys用户传输层
 *
 * @author wzkris
 */
@Data
public class SysUserDTO {

    private Long userId;

    private Long tenantId;

    private Long deptId;

    private String username;

    private String nickname;

    private String email;

    private String phoneNumber;

    private String status;

    private String gender;

    private String avatar;

    private String password;
}

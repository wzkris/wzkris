package com.wzkris.user.api.domain.response;

import lombok.Data;

import java.io.Serializable;

/**
 * sys用户信息
 *
 * @author wzkris
 */
@Data
public class SysUserResp implements Serializable {

    private Long userId;

    private Long tenantId;

    private Long deptId;

    private String username;

    private String nickname;

    private String email;

    private String phoneNumber;

    private String status;

    private String password;

    private String tenantStatus;

    private Long tenantExpired;

    private String packageStatus;

}

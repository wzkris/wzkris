package com.wzkris.user.rmi.domain.resp;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

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

    private Date tenantExpired;

    private String packageStatus;
}

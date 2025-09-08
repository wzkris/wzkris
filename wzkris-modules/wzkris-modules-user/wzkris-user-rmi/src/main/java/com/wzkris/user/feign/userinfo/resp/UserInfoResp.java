package com.wzkris.user.feign.userinfo.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * sys用户信息
 *
 * @author wzkris
 */
@Data
public class UserInfoResp implements Serializable {

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

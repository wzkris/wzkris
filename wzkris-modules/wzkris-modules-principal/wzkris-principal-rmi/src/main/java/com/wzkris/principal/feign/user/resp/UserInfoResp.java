package com.wzkris.principal.feign.user.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * sys用户信息
 *
 * @author wzkris
 */
@Data
public class UserInfoResp implements Serializable {

    private Long userId;

    private Long deptId;

    private String username;

    private String nickname;

    private String email;

    private String phoneNumber;

    private String status;

    private String password;

}

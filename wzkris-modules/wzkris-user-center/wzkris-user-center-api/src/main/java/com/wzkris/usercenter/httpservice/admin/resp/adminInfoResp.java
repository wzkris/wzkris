package com.wzkris.usercenter.httpservice.admin.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * sys用户信息
 *
 * @author wzkris
 */
@Data
public class adminInfoResp implements Serializable {

    private Long adminId;

    private Long deptId;

    private String username;

    private String nickname;

    private String email;

    private String phoneNumber;

    private String status;

    private String password;

}

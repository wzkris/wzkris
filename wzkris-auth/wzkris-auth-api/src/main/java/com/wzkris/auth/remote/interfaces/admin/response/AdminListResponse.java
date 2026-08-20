package com.wzkris.auth.remote.interfaces.admin.response;

import lombok.Data;

import java.io.Serializable;

/**
 * sys用户信息
 *
 * @author wzkris
 */
@Data
public class AdminListResponse implements Serializable {

    private Long id;

    private Long deptId;

    private String username;

    private String nickname;

    private String email;

    private String phoneNumber;

    private String status;

    private String password;

}


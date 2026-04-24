package com.wzkris.usercenter.remote.api.admin.response;

import com.wzkris.usercenter.enums.admin.AdminStatusEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class AdminInfoResponse implements Serializable {

    private Long adminId;

    private Long deptId;

    private String username;

    private String nickname;

    private String email;

    private String phoneNumber;

    private AdminStatusEnum status;

    private String password;

}

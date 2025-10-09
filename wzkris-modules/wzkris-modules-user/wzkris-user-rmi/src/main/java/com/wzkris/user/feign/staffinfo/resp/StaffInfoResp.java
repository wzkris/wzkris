package com.wzkris.user.feign.staffinfo.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * sys用户信息
 *
 * @author wzkris
 */
@Data
public class StaffInfoResp implements Serializable {

    private Long staffId;

    private Long tenantId;

    private String staffName;

    private String phoneNumber;

    private String status;

    private String password;

    private String tenantStatus;

    private Date tenantExpired;

    private String packageStatus;

}

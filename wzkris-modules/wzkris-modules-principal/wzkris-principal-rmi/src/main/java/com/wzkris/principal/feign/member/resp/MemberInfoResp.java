package com.wzkris.principal.feign.member.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * sys用户信息
 *
 * @author wzkris
 */
@Data
public class MemberInfoResp implements Serializable {

    private Long memberId;

    private Long tenantId;

    private String username;

    private String phoneNumber;

    private String status;

    private String password;

    private String tenantStatus;

    private Date tenantExpired;

    private String packageStatus;

}

package com.wzkris.usercenter.remote.api.member.response;

import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
public class MemberInfoResponse implements Serializable {

    private Long memberId;

    private Long tenantId;

    private String username;

    private String phoneNumber;

    private String status;

    private String password;

    private String tenantStatus;

    private OffsetDateTime tenantExpired;

    private String packageStatus;

}

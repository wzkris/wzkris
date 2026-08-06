package com.wzkris.usercenter.remote.api.member.response;

import com.wzkris.usercenter.enums.member.MemberStatusEnum;
import com.wzkris.usercenter.enums.tenant.TenantStatusEnum;
import com.wzkris.usercenter.enums.tenantpackage.TenantPackageStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
public class MemberQueryResponse implements Serializable {

    private Long id;

    private Long tenantId;

    private String username;

    private String phoneNumber;

    private MemberStatusEnum status;

    private String password;

    private TenantStatusEnum tenantStatus;

    private OffsetDateTime tenantExpired;

    private TenantPackageStatusEnum packageStatus;

}

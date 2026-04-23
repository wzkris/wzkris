package com.wzkris.usercenter.response.tenantwallet;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class TenantWalletRecordResponse {

    private Long recordId;

    private Long tenantId;

    private BigDecimal amount;

    private String recordType;

    private String bizType;

    private String bizNo;

    private OffsetDateTime createAt;

    private String remark;

}

package com.wzkris.usercenter.response.tenantwallet;

import com.wzkris.usercenter.enums.wallet.WalletRecordTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class TenantWalletRecordResponse {

    private Long recordId;

    private Long tenantId;

    private BigDecimal amount;

    private WalletRecordTypeEnum recordType;

    private String bizType;

    private String bizNo;

    private OffsetDateTime createAt;

    private String remark;

}

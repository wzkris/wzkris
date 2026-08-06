package com.wzkris.usercenter.api.tenantwallet.response;

import com.wzkris.usercenter.enums.wallet.WalletRecordTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
public class TenantWalletRecordInfoPageResponse {

    private Long id;

    private Long tenantId;

    private BigDecimal amount;

    private WalletRecordTypeEnum recordType;

    private String bizType;

    private String bizNo;

    private OffsetDateTime createAt;

    private String remark;
}

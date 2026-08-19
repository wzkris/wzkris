package com.wzkris.payment.api.tenantbalance.response;

import com.wzkris.payment.enums.balance.BalanceRecordTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
public class TenantBalanceTransactionLogMngPageResponse {

    private Long id;

    private Long tenantId;

    private BigDecimal amount;

    private BalanceRecordTypeEnum recordType;

    private String bizType;

    private String bizNo;

    private OffsetDateTime createAt;

    private String remark;

}
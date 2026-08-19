package com.wzkris.payment.api.customerbalance.response;

import com.wzkris.payment.enums.balance.BalanceRecordTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
public class CustomerBalanceTransactionLogInfoPageResponse {

    private Long id;

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "金额, 元")
    private BigDecimal amount;

    @Schema(description = "记录类型")
    private BalanceRecordTypeEnum recordType;

    @Schema(description = "创建时间")
    private OffsetDateTime createAt;

    @Schema(description = "备注")
    private String remark;

}
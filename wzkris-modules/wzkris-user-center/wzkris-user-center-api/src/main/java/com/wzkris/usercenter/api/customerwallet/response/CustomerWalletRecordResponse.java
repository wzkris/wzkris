package com.wzkris.usercenter.api.customerwallet.response;

import com.wzkris.usercenter.enums.wallet.WalletRecordTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
public class CustomerWalletRecordResponse {

    private Long recordId;

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "金额, 元")
    private BigDecimal amount;

    @Schema(description = "记录类型")
    private WalletRecordTypeEnum recordType;

    @Schema(description = "创建时间")
    private OffsetDateTime createAt;

    @Schema(description = "备注")
    private String remark;

}

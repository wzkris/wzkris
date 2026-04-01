package com.wzkris.usercenter.response.customerwallet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
public class CustomerWalletRecordResponse {

    private Long recordId;

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "金额, 元")
    private BigDecimal amount;

    @Schema(description = "记录类型 0-收入 1-支出")
    private String recordType;

    @Schema(description = "创建时间")
    private Date createAt;

    @Schema(description = "备注")
    private String remark;

}

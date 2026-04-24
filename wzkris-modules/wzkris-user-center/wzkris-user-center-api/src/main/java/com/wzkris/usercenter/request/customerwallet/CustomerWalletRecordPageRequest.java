package com.wzkris.usercenter.request.customerwallet;

import com.wzkris.common.core.model.QueryRequest;
import com.wzkris.common.validator.annotation.EnumsCheck;
import com.wzkris.usercenter.enums.wallet.WalletRecordTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CustomerWalletRecordPageRequest extends QueryRequest {

    @EnumsCheck(value = WalletRecordTypeEnum.class, property = "value")
    @Schema(description = "记录类型 0-收入 1-支出")
    private String recordType;

}


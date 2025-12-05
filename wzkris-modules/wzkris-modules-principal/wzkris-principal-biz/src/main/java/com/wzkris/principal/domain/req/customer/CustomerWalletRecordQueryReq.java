package com.wzkris.principal.domain.req.customer;

import com.wzkris.common.validator.annotation.EnumsCheck;
import com.wzkris.common.web.model.QueryReq;
import com.wzkris.principal.enums.WalletRecordTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CustomerWalletRecordQueryReq extends QueryReq {

    @EnumsCheck(value = WalletRecordTypeEnum.class, property = "value")
    @Schema(description = "记录类型 0-收入 1-支出")
    private String recordType;

}

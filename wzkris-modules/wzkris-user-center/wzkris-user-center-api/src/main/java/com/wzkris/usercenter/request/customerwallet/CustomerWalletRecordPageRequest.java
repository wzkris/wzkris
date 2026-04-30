package com.wzkris.usercenter.request.customerwallet;

import com.wzkris.common.core.model.QueryRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CustomerWalletRecordPageRequest extends QueryRequest {

    @Schema(description = "记录类型 0-收入 1-支出")
    private String recordType;

}


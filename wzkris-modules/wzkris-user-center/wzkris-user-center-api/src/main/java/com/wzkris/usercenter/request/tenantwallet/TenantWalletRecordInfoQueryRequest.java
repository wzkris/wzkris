package com.wzkris.usercenter.request.tenantwallet;

import com.wzkris.common.core.model.QueryRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 钱包记录筛选条件
 */
@Data
public class TenantWalletRecordInfoQueryRequest extends QueryRequest {

    @Schema(description = "记录类型 0-收入 1-支出")
    private String recordType;

}

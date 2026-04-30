package com.wzkris.usercenter.request.tenantwallet;

import com.wzkris.common.core.model.QueryRequest;
import com.wzkris.usercenter.enums.wallet.WalletRecordTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 钱包记录筛选条件
 */
@Data
public class TenantWalletRecordMngPageRequest extends QueryRequest {

    private Long tenantId;

    @Schema(description = "记录类型")
    private WalletRecordTypeEnum recordType;

}

package com.wzkris.principal.domain.req.tenantwallet;

import com.wzkris.common.web.model.QueryReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 钱包记录筛选条件
 */
@Data
public class TenantWalletRecordQueryReq extends QueryReq {

    private Long tenantId;

    @Schema(description = "记录类型 0-收入 1-支出")
    private String recordType;

}

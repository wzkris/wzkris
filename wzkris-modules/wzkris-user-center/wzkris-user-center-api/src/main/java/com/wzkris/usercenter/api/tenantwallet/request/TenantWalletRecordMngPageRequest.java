package com.wzkris.usercenter.api.tenantwallet.request;

import com.wzkris.common.orm.request.PagingRequest;
import com.wzkris.usercenter.enums.wallet.WalletRecordTypeEnum;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "租户钱包记录分页查询条件（manage）")
public class TenantWalletRecordMngPageRequest extends PagingRequest {

    @Parameter(description = "租户ID")
    private Long tenantId;

    @Parameter(description = "记录类型")
    private WalletRecordTypeEnum recordType;

}

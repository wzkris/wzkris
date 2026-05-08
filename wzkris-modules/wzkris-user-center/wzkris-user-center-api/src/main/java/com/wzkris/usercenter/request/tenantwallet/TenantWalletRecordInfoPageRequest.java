package com.wzkris.usercenter.request.tenantwallet;

import com.wzkris.common.orm.request.PagingRequest;
import com.wzkris.usercenter.enums.wallet.WalletRecordTypeEnum;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "租户钱包记录分页查询条件（info）")
public class TenantWalletRecordInfoPageRequest extends PagingRequest {

    @Parameter(description = "记录类型")
    private WalletRecordTypeEnum recordType;
}

package com.wzkris.payment.api.tenantbalance.request;

import com.wzkris.common.orm.request.PagingRequest;
import com.wzkris.payment.enums.balance.BalanceRecordTypeEnum;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "租户余额记录分页查询条件（info）")
public class TenantBalanceTransactionLogInfoPageRequest extends PagingRequest {

    @Parameter(description = "记录类型")
    private BalanceRecordTypeEnum recordType;

}
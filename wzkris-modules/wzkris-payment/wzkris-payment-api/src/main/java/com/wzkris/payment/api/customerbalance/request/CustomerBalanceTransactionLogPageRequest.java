package com.wzkris.payment.api.customerbalance.request;

import com.wzkris.common.orm.request.PagingRequest;
import com.wzkris.payment.enums.balance.BalanceRecordTypeEnum;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "客户余额记录分页查询条件")
public class CustomerBalanceTransactionLogPageRequest extends PagingRequest {

    @Parameter(description = "记录类型")
    private BalanceRecordTypeEnum recordType;

}
package com.wzkris.payment.api.customerbalance;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.payment.api.customerbalance.request.CustomerBalanceTransactionLogPageRequest;
import com.wzkris.payment.api.customerbalance.response.CustomerBalanceInfoQueryResponse;
import com.wzkris.payment.api.customerbalance.response.CustomerBalanceTransactionLogInfoPageResponse;

public interface CustomerBalanceInfoApi {

    Result<CustomerBalanceInfoQueryResponse> query();

    Result<Page<CustomerBalanceTransactionLogInfoPageResponse>> queryTransactionPage(CustomerBalanceTransactionLogPageRequest request);

}
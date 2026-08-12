package com.wzkris.payment.api.tenantbalance;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.payment.api.tenantbalance.request.TenantBalanceTransactionLogMngPageRequest;
import com.wzkris.payment.api.tenantbalance.response.TenantBalanceTransactionLogMngPageResponse;

public interface TenantBalanceMngApi {

    Result<Page<TenantBalanceTransactionLogMngPageResponse>> queryTransactionPage(TenantBalanceTransactionLogMngPageRequest request);

}
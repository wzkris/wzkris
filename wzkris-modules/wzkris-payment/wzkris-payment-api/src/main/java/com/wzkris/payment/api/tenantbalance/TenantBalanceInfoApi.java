package com.wzkris.payment.api.tenantbalance;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.payment.api.tenantbalance.request.BalanceWithdrawalRequest;
import com.wzkris.payment.api.tenantbalance.request.SetPayPasswordRequest;
import com.wzkris.payment.api.tenantbalance.request.TenantBalanceTransactionLogInfoPageRequest;
import com.wzkris.payment.api.tenantbalance.response.TenantBalanceInfoQueryResponse;
import com.wzkris.payment.api.tenantbalance.response.TenantBalanceTransactionLogInfoPageResponse;

public interface TenantBalanceInfoApi {

    Result<TenantBalanceInfoQueryResponse> query();

    Result<Page<TenantBalanceTransactionLogInfoPageResponse>> queryTransactionPage(TenantBalanceTransactionLogInfoPageRequest request);

    /**
     * 设置/修改提现支付密码
     */
    Result<Void> setPayPassword(SetPayPasswordRequest request);

    /**
     * 提现（校验支付密码 + 记账；打款对接后续）
     */
    Result<Void> withdrawal(BalanceWithdrawalRequest request);

}
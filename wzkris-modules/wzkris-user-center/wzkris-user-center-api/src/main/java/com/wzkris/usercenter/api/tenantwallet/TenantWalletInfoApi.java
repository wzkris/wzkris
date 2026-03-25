package com.wzkris.usercenter.api.tenantwallet;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.request.tenantwallet.TenantWalletRecordQueryRequest;
import com.wzkris.usercenter.request.tenantwallet.WalletWithdrawalRequest;
import com.wzkris.usercenter.response.tenantwallet.TenantWalletInfoResponse;
import com.wzkris.usercenter.response.tenantwallet.TenantWalletRecordResponse;

public interface TenantWalletInfoApi {

    TenantWalletInfoResponse queryInfo();

    Result<Page<TenantWalletRecordResponse>> queryRecordPage(TenantWalletRecordQueryRequest request);

    Result<Void> withdrawal(WalletWithdrawalRequest request);

}

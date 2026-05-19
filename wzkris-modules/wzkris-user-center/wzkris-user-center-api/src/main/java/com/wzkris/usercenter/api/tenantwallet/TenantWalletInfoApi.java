package com.wzkris.usercenter.api.tenantwallet;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.tenantwallet.request.TenantWalletRecordInfoPageRequest;
import com.wzkris.usercenter.api.tenantwallet.request.WalletWithdrawalRequest;
import com.wzkris.usercenter.api.tenantwallet.response.TenantWalletInfoResponse;
import com.wzkris.usercenter.api.tenantwallet.response.TenantWalletRecordResponse;

public interface TenantWalletInfoApi {

    Result<TenantWalletInfoResponse> queryInfo();

    Result<Page<TenantWalletRecordResponse>> queryRecordPage(TenantWalletRecordInfoPageRequest request);

    Result<Void> withdrawal(WalletWithdrawalRequest request);

}

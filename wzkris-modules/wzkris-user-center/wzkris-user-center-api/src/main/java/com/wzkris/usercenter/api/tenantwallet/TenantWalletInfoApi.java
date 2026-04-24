package com.wzkris.usercenter.api.tenantwallet;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.request.tenantwallet.TenantWalletRecordInfoPageRequest;
import com.wzkris.usercenter.request.tenantwallet.WalletWithdrawalRequest;
import com.wzkris.usercenter.response.tenantwallet.TenantWalletInfoResponse;
import com.wzkris.usercenter.response.tenantwallet.TenantWalletRecordResponse;

public interface TenantWalletInfoApi {

    Result<TenantWalletInfoResponse> queryInfo();

    Result<Page<TenantWalletRecordResponse>> queryRecordPage(TenantWalletRecordInfoPageRequest request);

    Result<Void> withdrawal(WalletWithdrawalRequest request);

}

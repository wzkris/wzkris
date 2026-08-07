package com.wzkris.usercenter.api.tenantwallet;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.tenantwallet.request.TenantWalletRecordInfoPageRequest;
import com.wzkris.usercenter.api.tenantwallet.request.WalletWithdrawalRequest;
import com.wzkris.usercenter.api.tenantwallet.response.TenantWalletInfoQueryResponse;
import com.wzkris.usercenter.api.tenantwallet.response.TenantWalletRecordInfoPageResponse;

public interface TenantWalletInfoApi {

    Result<TenantWalletInfoQueryResponse> query();

    Result<Page<TenantWalletRecordInfoPageResponse>> queryRecordPage(TenantWalletRecordInfoPageRequest request);

    Result<Void> withdrawal(WalletWithdrawalRequest request);

}

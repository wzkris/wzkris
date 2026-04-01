package com.wzkris.usercenter.api.tenantwallet;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.request.tenantwallet.TenantWalletRecordMngQueryRequest;
import com.wzkris.usercenter.response.tenantwallet.TenantWalletRecordResponse;

public interface TenantWalletMngApi {

    Result<Page<TenantWalletRecordResponse>> queryRecordPage(TenantWalletRecordMngQueryRequest request);

}

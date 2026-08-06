package com.wzkris.usercenter.api.tenantwallet;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.tenantwallet.request.TenantWalletRecordMngPageRequest;
import com.wzkris.usercenter.api.tenantwallet.response.TenantWalletRecordMngPageResponse;

public interface TenantWalletMngApi {

    Result<Page<TenantWalletRecordMngPageResponse>> queryRecordPage(TenantWalletRecordMngPageRequest request);

}

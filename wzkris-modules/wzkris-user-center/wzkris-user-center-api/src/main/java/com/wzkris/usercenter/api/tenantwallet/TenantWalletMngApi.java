package com.wzkris.usercenter.api.tenantwallet;

import com.wzkris.usercenter.request.tenantwallet.TenantWalletRecordQueryRequest;
import com.wzkris.usercenter.response.tenantwallet.TenantWalletRecordResponse;

import java.util.List;

public interface TenantWalletMngApi {

    List<TenantWalletRecordResponse> listRecord(TenantWalletRecordQueryRequest request);

}

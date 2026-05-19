package com.wzkris.usercenter.api.customerwallet;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.customerwallet.request.CustomerWalletRecordPageRequest;
import com.wzkris.usercenter.api.customerwallet.response.CustomerWalletInfoResponse;
import com.wzkris.usercenter.api.customerwallet.response.CustomerWalletRecordResponse;

public interface CustomerWalletInfoApi {

    Result<CustomerWalletInfoResponse> queryInfo();

    Result<Page<CustomerWalletRecordResponse>> queryRecordPage(CustomerWalletRecordPageRequest request);

}

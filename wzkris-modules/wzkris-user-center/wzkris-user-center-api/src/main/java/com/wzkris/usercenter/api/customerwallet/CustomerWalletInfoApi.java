package com.wzkris.usercenter.api.customerwallet;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.request.customerwallet.CustomerWalletRecordPageRequest;
import com.wzkris.usercenter.response.customerwallet.CustomerWalletInfoResponse;
import com.wzkris.usercenter.response.customerwallet.CustomerWalletRecordResponse;

public interface CustomerWalletInfoApi {

    Result<CustomerWalletInfoResponse> queryInfo();

    Result<Page<CustomerWalletRecordResponse>> queryRecordPage(CustomerWalletRecordPageRequest request);

}

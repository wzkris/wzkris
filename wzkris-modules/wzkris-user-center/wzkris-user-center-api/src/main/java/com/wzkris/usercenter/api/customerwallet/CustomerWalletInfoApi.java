package com.wzkris.usercenter.api.customerwallet;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.customerwallet.request.CustomerWalletRecordPageRequest;
import com.wzkris.usercenter.api.customerwallet.response.CustomerWalletInfoQueryResponse;
import com.wzkris.usercenter.api.customerwallet.response.CustomerWalletRecordInfoPageResponse;

public interface CustomerWalletInfoApi {

    Result<CustomerWalletInfoQueryResponse> queryInfo();

    Result<Page<CustomerWalletRecordInfoPageResponse>> queryRecordPage(CustomerWalletRecordPageRequest request);

}

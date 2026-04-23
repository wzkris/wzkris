package com.wzkris.auth.api;

import com.wzkris.auth.response.QrTokenResponse;
import com.wzkris.common.core.model.Result;

public interface QrLoginApi {

    Result<?> qrcode();

    Result<Void> scan(String qrcodeId);

    Result<Void> confirm(String qrcodeId);

    Result<QrTokenResponse> pollstatus(String qrcodeId);

}
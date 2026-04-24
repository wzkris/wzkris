package com.wzkris.auth.api;

import com.wzkris.auth.request.QrCodeIdRequest;
import com.wzkris.auth.response.QrTokenResponse;
import com.wzkris.common.core.model.Result;

public interface QrLoginApi {

    Result<?> qrcode();

    Result<Void> scan(QrCodeIdRequest request);

    Result<Void> confirm(QrCodeIdRequest request);

    Result<QrTokenResponse> pollstatus(QrCodeIdRequest request);

}
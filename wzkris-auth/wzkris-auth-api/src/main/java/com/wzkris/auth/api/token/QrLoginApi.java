package com.wzkris.auth.api.token;

import com.wzkris.auth.api.token.request.QrCodeIdRequest;
import com.wzkris.auth.api.token.response.QrTokenResponse;
import com.wzkris.common.core.model.Result;

public interface QrLoginApi {

    Result<?> qrcode();

    Result<Void> scan(QrCodeIdRequest request);

    Result<Void> confirm(QrCodeIdRequest request);

    Result<QrTokenResponse> pollstatus(QrCodeIdRequest request);

}
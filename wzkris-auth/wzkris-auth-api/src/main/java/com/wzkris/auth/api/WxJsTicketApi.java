package com.wzkris.auth.api;

import com.wzkris.common.core.model.Result;

public interface WxJsTicketApi {

    Result<?> queryJsticket();

    Result<?> queryJsapiSignature(String url);

}
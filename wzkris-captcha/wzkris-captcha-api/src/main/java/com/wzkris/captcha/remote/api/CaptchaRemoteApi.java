package com.wzkris.captcha.remote.api;

import com.wzkris.captcha.remote.api.request.CaptchaCheckRequest;
import com.wzkris.common.core.model.Result;

public interface CaptchaRemoteApi {

    Result<Boolean> check(CaptchaCheckRequest request);

}

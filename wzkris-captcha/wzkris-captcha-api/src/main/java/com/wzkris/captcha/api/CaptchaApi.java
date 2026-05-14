package com.wzkris.captcha.api;

import com.wzkris.captcha.request.CaptchaDispatchRequest;
import com.wzkris.captcha.response.CaptchaDispatchResponse;

public interface CaptchaApi {

    CaptchaDispatchResponse dispatch(CaptchaDispatchRequest request);

}

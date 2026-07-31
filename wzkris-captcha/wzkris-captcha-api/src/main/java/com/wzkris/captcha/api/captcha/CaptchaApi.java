package com.wzkris.captcha.api.captcha;

import com.wzkris.captcha.api.captcha.request.CaptchaDispatchRequest;
import com.wzkris.captcha.api.captcha.response.CaptchaDispatchResponse;

public interface CaptchaApi {

    CaptchaDispatchResponse dispatch(CaptchaDispatchRequest request);

}

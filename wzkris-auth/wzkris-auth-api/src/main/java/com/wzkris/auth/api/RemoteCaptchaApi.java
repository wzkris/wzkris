package com.wzkris.auth.api;

import com.wzkris.auth.api.domain.request.SmsCheckReq;
import com.wzkris.common.core.exception.captcha.CaptchaException;
import jakarta.validation.Valid;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 验证码服务
 * @date : 2023/8/21 11:27
 */
public interface RemoteCaptchaApi {

    /**
     * 校验手机号验证码
     */
    boolean validateSms(@Valid SmsCheckReq smsCheckReq) throws CaptchaException;
}

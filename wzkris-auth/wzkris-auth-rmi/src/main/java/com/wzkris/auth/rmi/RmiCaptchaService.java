package com.wzkris.auth.rmi;

import com.wzkris.auth.rmi.domain.req.SmsCheckReq;
import com.wzkris.common.core.exception.captcha.CaptchaException;
import com.wzkris.common.openfeign.constants.ApplicationConstant;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 验证码服务
 * @date : 2023/8/21 11:27
 */
@FeignClient(name = ApplicationConstant.AUTH, contextId = "RmiCaptchaService")
public interface RmiCaptchaService {

    /**
     * 校验手机号验证码
     */
    @PostMapping("/validate_sms")
    boolean validateSms(@Valid SmsCheckReq smsCheckReq) throws CaptchaException;
}

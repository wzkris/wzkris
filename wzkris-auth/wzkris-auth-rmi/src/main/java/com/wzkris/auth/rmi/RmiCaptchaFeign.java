package com.wzkris.auth.rmi;

import com.wzkris.auth.rmi.domain.req.SmsCheckReq;
import com.wzkris.auth.rmi.fallback.RmiCaptchaFeignFeignFallback;
import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import com.wzkris.common.openfeign.core.RmiFeign;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 验证码服务
 * @date : 2023/8/21 11:27
 */
@FeignClient(name = ServiceIdConstant.AUTH, contextId = "RmiCaptchaFeign", fallbackFactory = RmiCaptchaFeignFeignFallback.class)
public interface RmiCaptchaFeign extends RmiFeign {

    String prefix = "/rmi_captcha";

    /**
     * 校验手机号验证码
     */
    @PostMapping(prefix + "/validate_sms")
    boolean validateSms(@Valid @RequestBody SmsCheckReq smsCheckReq);

}

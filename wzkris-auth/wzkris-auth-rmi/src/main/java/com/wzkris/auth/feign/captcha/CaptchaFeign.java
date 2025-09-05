package com.wzkris.auth.feign.captcha;

import com.wzkris.auth.feign.captcha.fallback.CaptchaFeignFeignFallback;
import com.wzkris.auth.feign.captcha.req.CaptchaCheckReq;
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
@FeignClient(name = ServiceIdConstant.AUTH, contextId = "CaptchaFeign",
        fallbackFactory = CaptchaFeignFeignFallback.class,
        path = "/feign-captcha")
public interface CaptchaFeign extends RmiFeign {

    /**
     * 校验手机号验证码
     */
    @PostMapping("/validate")
    boolean validateCaptcha(@Valid @RequestBody CaptchaCheckReq captchaCheckReq);

}

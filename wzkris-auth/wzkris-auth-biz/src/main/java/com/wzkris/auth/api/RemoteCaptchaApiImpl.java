package com.wzkris.auth.api;

import com.wzkris.auth.api.domain.SmsDTO;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.security.annotation.InnerAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import static com.wzkris.common.core.domain.Result.success;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 内部验证码接口
 * @date : 2023/8/21 13:05
 */
@InnerAuth
@RestController
@RequiredArgsConstructor
public class RemoteCaptchaApiImpl implements RemoteCaptchaApi {
    private final CaptchaService captchaService;

    /**
     * 校验手机号
     */
    @Override
    public Result<Void> validateSms(SmsDTO smsDTO) {
        captchaService.validateSmsCode(smsDTO.getPhoneNumber(), smsDTO.getSmsCode());
        return success();
    }
}

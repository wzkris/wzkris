package com.wzkris.auth.api.fallback;

import com.wzkris.auth.api.RemoteCaptchaApi;
import com.wzkris.auth.api.domain.dto.SmsDTO;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.enums.BizCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import static com.wzkris.common.core.domain.Result.resp;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 验证码降级
 * @date : 2023/8/21 13:03
 */
@Slf4j
@Component
public class RemoteCaptchaApiFallback implements FallbackFactory<RemoteCaptchaApi> {

    @Override
    public RemoteCaptchaApi create(Throwable cause) {
        log.error("-----------认证服务发生熔断-----------");
        return new RemoteCaptchaApi() {
            @Override
            public Result<Void> validateSms(SmsDTO smsDTO) {
                log.error("验证短信发生异常，errMsg：{}", cause.getMessage());

                return resp(BizCode.RPC_INVOCATION, cause.getMessage());
            }
        };
    }
}

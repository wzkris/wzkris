package com.thingslink.auth.api.fallback;

import com.thingslink.auth.api.RemoteCaptchaApi;
import com.thingslink.auth.api.domain.SmsDTO;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.enums.BizCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import static com.thingslink.common.core.domain.Result.resp;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description :
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
                log.error("验证短信发生异常，errMsg：{}", cause.getMessage(), cause);

                return resp(BizCode.RPC_INVOCATION, cause.getMessage());
            }
        };
    }
}

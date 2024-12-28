package com.wzkris.auth.api.fallback;

import com.wzkris.auth.api.RemoteCaptchaApi;
import com.wzkris.auth.api.domain.request.SmsCheckReq;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.openfeign.utils.FeignErrorUtil;
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

        return new RemoteCaptchaApi() {
            @Override
            public Result<Void> validateSms(SmsCheckReq smsCheckReq) {
                log.error("验证短信发生异常，errMsg：{}", cause.getMessage());

                return resp(FeignErrorUtil.getCode(cause), null, cause.getMessage());
            }
        };
    }

}

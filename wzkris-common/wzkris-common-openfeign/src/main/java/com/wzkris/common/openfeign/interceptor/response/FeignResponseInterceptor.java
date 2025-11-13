package com.wzkris.common.openfeign.interceptor.response;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.openfeign.enums.BizRpcCodeEnum;
import com.wzkris.common.openfeign.event.FeignResponseEvent;
import com.wzkris.common.openfeign.exception.RpcException;
import feign.InvocationContext;
import feign.Response;
import feign.ResponseInterceptor;
import feign.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Optional;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : openfeign响应处理
 * @date : 2025/06/11 16:50
 */
@Slf4j
public class FeignResponseInterceptor implements ResponseInterceptor {

    @Override
    public Object intercept(InvocationContext invocationContext, Chain chain) throws Exception {
        try (Response response = invocationContext.response()) {
            Optional<String> reqTime = response.request().headers().get(CustomHeaderConstants.X_REQUEST_TIME).stream().findFirst();
            long costTime = System.currentTimeMillis() - Long.parseLong(reqTime.get());

            checkSuccess(response, costTime);

            Object next = chain.next(invocationContext);

            // 打印响应信息
            logResponseInfo(response, next, costTime);

            return next;
        }
    }

    private void checkSuccess(Response response, long costTime) throws IOException {
        if (!HttpStatus.valueOf(response.status()).is2xxSuccessful()) {
            String body = Util.toString(response.body().asReader(response.charset()));
            log.error("""
                            Feign Called Failed => Url: {}
                            Response: {}
                            Response Body: {}
                            Taken Time: {} ms
                            """,
                    response.request().url(),
                    response,
                    body,
                    costTime
            );

            FeignResponseEvent responseEvent = new FeignResponseEvent();
            responseEvent.setSuccess(false);
            responseEvent.setHttpStatusCode(response.status());
            responseEvent.setBody(body);
            responseEvent.setCostTime((int) costTime);

            SpringUtil.getContext().publishEvent(responseEvent);

            throw new RpcException(response.status(), BizRpcCodeEnum.RPC_REMOTE_ERROR.value(),
                    body);
        }
    }

    /**
     * 打印Feign响应信息
     */
    private void logResponseInfo(Response response, Object next, long costTime) {
        try {
            log.info("""
                            Feign Called Success =>  Url: {}
                            Response: {}
                            Response Body: {}
                            Taken Time: {} ms
                            """,
                    response.request().url(),
                    response,
                    next,
                    costTime
            );

            FeignResponseEvent responseEvent = new FeignResponseEvent();
            responseEvent.setSuccess(false);
            responseEvent.setHttpStatusCode(response.status());
            responseEvent.setBody(JsonUtil.toJsonString(next));
            responseEvent.setCostTime((int) costTime);

            SpringUtil.getContext().publishEvent(responseEvent);
        } catch (Exception e) {
            log.warn("Feign Response => 打印响应发生异常: {}", e.getMessage(), e);
        }
    }

}

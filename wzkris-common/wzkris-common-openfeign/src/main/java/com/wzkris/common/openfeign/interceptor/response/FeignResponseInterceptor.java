package com.wzkris.common.openfeign.interceptor.response;

import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.openfeign.enums.BizRpcCode;
import com.wzkris.common.openfeign.exception.RpcException;
import feign.InvocationContext;
import feign.Response;
import feign.ResponseInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
        try (Response originalResponse = invocationContext.response()) {
            checkSuccess(originalResponse);

            Object next = chain.next(invocationContext);

            // 打印响应信息
            logResponseInfo(originalResponse, next);

            return next;
        }
    }

    private void checkSuccess(Response originalResponse) throws IOException {
        if (!HttpStatus.valueOf(originalResponse.status()).is2xxSuccessful()) {
            String resultBody = new String(originalResponse.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
            log.error("""
                            feign called failed => Url: {}
                            Response: {}
                            Response body: {}
                            """,
                    originalResponse.request().url(),
                    originalResponse,
                    resultBody
            );
            throw new RpcException(BizRpcCode.RPC_REMOTE_ERROR.value(), JsonUtil.parseObject(resultBody, Result.class));
        }
    }

    /**
     * 打印Feign响应信息
     */
    private void logResponseInfo(Response response, Object next) {
        try {
            Optional<String> reqTime = response.request().headers().get(HeaderConstants.X_REQUEST_TIME).stream().findFirst();
            log.info("""
                            Feign called Success =>  Url: {}
                            Response: {}
                            Response Body: {}
                            Taken time: {} ms
                            """,
                    response.request().url(), response, next, System.currentTimeMillis() - Long.parseLong(reqTime.get()));
        } catch (Exception e) {
            log.warn("Feign Response => 打印响应发生异常: {}", e.getMessage(), e);
        }
    }

}

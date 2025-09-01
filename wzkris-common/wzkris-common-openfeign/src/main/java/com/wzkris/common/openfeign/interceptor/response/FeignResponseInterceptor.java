package com.wzkris.common.openfeign.interceptor.response;

import com.wzkris.common.openfeign.constants.FeignHeaderConstant;
import com.wzkris.common.openfeign.enums.BizRpcCode;
import com.wzkris.common.openfeign.exception.RpcException;
import feign.InvocationContext;
import feign.Response;
import feign.ResponseInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
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
            Collection<String> ex = response.headers().get(FeignHeaderConstant.X_FEIGN_EXCEPTION);
            if (CollectionUtils.isNotEmpty(ex)) {
                Optional<String> first = ex.stream().findFirst();
                throw new RpcException(BizRpcCode.RPC_REMOTE_ERROR.value(), URLDecoder.decode(first.get(), StandardCharsets.UTF_8));
            }
            if (!HttpStatus.valueOf(response.status()).is2xxSuccessful()) {
                log.error("feign called failed, <request> => {} " +
                        " <response> => {}", response.request(), response);
                throw new RpcException(BizRpcCode.RPC_REMOTE_ERROR.value(), BizRpcCode.RPC_REMOTE_ERROR.desc());
            }
            return chain.next(invocationContext);
        }
    }

}

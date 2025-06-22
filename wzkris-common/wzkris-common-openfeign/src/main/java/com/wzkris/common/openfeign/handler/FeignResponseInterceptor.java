package com.wzkris.common.openfeign.handler;

import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.openfeign.exception.RpcException;
import feign.InvocationContext;
import feign.Response;
import feign.ResponseInterceptor;

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
public class FeignResponseInterceptor implements ResponseInterceptor {

    @Override
    public Object intercept(InvocationContext invocationContext, Chain chain) throws Exception {
        try (Response response = invocationContext.response()) {
            Collection<String> ex = response.headers().get(HeaderConstants.X_FEIGN_EXCEPTION);
            if (ex != null && !ex.isEmpty()) {
                Optional<String> first = ex.stream().findFirst();
                throw new RpcException(BizCode.RPC_REMOTE_ERROR.value(), URLDecoder.decode(first.get(), StandardCharsets.UTF_8));
            }
            return chain.next(invocationContext);
        }
    }

}

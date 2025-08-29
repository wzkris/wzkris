package com.wzkris.common.sentinel.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.wzkris.common.core.enums.BizBaseCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : sentinel限流异常全局处理， 使用了@SentinelResource注解的不会触发
 * @date : 2023/12/7 14:31
 */
@Slf4j
@Component
public class SentinelExceptionHandler implements BlockExceptionHandler {

    public static final String RETRY_AFTER = "Retry-After";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, String s, BlockException e)
            throws Exception {
        log.warn("应用：‘{}’的接口‘{}’触发限流", e.getRule().getResource(), request.getRequestURI());
        response.setHeader(RETRY_AFTER, "10");
        response.sendError(BizBaseCode.TOO_MANY_REQUESTS.value(), BizBaseCode.TOO_MANY_REQUESTS.desc());
    }

}

package com.wzkris.common.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.utils.json.JsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : sentinel限流异常全局处理， 使用了@SentinelResource注解的不会触发
 * @date : 2023/12/7 14:31
 */
public class SentinelExceptionHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        Result<?> result;
        if (e instanceof AuthorityException) {
            result = Result.resp(BizCode.UNAUTHORIZED);
        }
        else {
            result = Result.resp(BizCode.BAD_REQUEST, "请求限流");
        }
        response.setContentType("application/json");
        response.setStatus(200);
        response.getWriter().write(JsonUtil.toJsonString(result));
    }
}
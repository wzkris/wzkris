package com.thingslink.common.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.enums.BizCode;
import com.thingslink.common.core.utils.json.JsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : sentinel限流异常全局处理， 使用了@SentinelResource注解的不会触发
 * @date : 2023/12/7 14:31
 */
public class SentinelExceptionHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        BizCode bizCode;
        if (e instanceof AuthorityException) {
            bizCode = BizCode.UNAUTHORIZED;
        }
        else {
            bizCode = BizCode.LIMIT_FLOW;
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(JsonUtil.toJsonString(Result.resp(bizCode)));
    }
}
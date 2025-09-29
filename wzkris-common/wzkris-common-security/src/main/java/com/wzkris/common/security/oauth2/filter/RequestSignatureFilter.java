package com.wzkris.common.security.oauth2.filter;

import com.wzkris.common.apikey.config.SignkeyProperties;
import com.wzkris.common.apikey.enums.BizSignCode;
import com.wzkris.common.apikey.utils.RequestSignerUtil;
import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.core.utils.TraceIdUtil;
import com.wzkris.common.security.oauth2.request.RepeatableReadRequestWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 请求签名过滤器
 *
 * @author wzkris
 */
@RequiredArgsConstructor
public class RequestSignatureFilter extends OncePerRequestFilter {

    private final SignkeyProperties signkeyProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        RepeatableReadRequestWrapper requestWrapper = (RepeatableReadRequestWrapper) request;

        final String requestFrom = requestWrapper.getHeader(HeaderConstants.X_REQUEST_FROM);
        final SignkeyProperties.Sign sign = signkeyProperties.getKeys().get(requestFrom);
        if (sign == null) {
            sendErrorResponse(response, Result.resp(BizSignCode.SIGN_NOT_EXIST.value(), null, BizSignCode.SIGN_NOT_EXIST.desc()));
            return;
        }

        // 1. 获取请求头中的签名和时间戳
        final String signature = requestWrapper.getHeader(HeaderConstants.X_REQUEST_SIGN);
        final String requestTime = requestWrapper.getHeader(HeaderConstants.X_REQUEST_TIME);

        // 2. 检查必要的请求头是否存在
        if (StringUtil.isBlank(signature) || StringUtil.isBlank(requestTime)) {
            sendErrorResponse(response, Result.resp(BizSignCode.SIGN_NOT_EXIST.value(), null, BizSignCode.SIGN_NOT_EXIST.desc()));
            return;
        }

        boolean verified = RequestSignerUtil.verifySignature(sign.getSecret(),
                TraceIdUtil.get(),
                requestWrapper.getBodyAsString(),
                Long.parseLong(requestTime),
                signature);

        if (!verified) {
            sendErrorResponse(response, Result.resp(BizSignCode.SIGN_ERROR.value(), null, BizSignCode.SIGN_ERROR.desc()));
            return;
        }

        filterChain.doFilter(requestWrapper, response);
    }

    private void sendErrorResponse(HttpServletResponse response, Result<?> result) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpStatus.FORBIDDEN.value());
        JsonUtil.writeValue(response.getWriter(), result);
    }

}

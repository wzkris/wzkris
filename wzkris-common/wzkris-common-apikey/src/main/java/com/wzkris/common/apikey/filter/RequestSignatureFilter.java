package com.wzkris.common.apikey.filter;

import com.wzkris.common.apikey.config.SignkeyProperties;
import com.wzkris.common.apikey.enums.BizSignCodeEnum;
import com.wzkris.common.apikey.request.RepeatableReadRequestWrapper;
import com.wzkris.common.apikey.utils.RequestSignerUtil;
import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequiredArgsConstructor
public class RequestSignatureFilter extends OncePerRequestFilter {

    private final SignkeyProperties signProp;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        RepeatableReadRequestWrapper requestWrapper = (RepeatableReadRequestWrapper) request;

        final String signature = requestWrapper.getHeader(CustomHeaderConstants.X_REQUEST_SIGN);

        if (StringUtil.isBlank(signature)) {
            sendErrorResponse(response, BizSignCodeEnum.SIGN_NOT_EXIST);
            return;
        }

        final String requestFrom = requestWrapper.getHeader(CustomHeaderConstants.X_REQUEST_FROM);
        final SignkeyProperties.Sign sign = signProp.getKeys().get(requestFrom);
        if (sign == null) {
            sendErrorResponse(response, BizSignCodeEnum.SECRET_ERROR);
            return;
        }

        // 1获取请求头中的时间戳和traceId
        final String requestTime = requestWrapper.getHeader(CustomHeaderConstants.X_REQUEST_TIME);
        final String traceId = request.getHeader(CustomHeaderConstants.X_TRACING_ID);

        if (StringUtil.isAnyBlank(requestTime, traceId)) {
            sendErrorResponse(response, BizSignCodeEnum.SIGN_HEADER_ERROR);
            return;
        }

        boolean verified = RequestSignerUtil.verifySignature(
                sign.getSecret(),
                traceId,
                requestWrapper.getBodyAsString(),
                Long.parseLong(requestTime),
                signature,
                sign.getMaxInterval()
        );

        if (!verified) {
            sendErrorResponse(response, BizSignCodeEnum.SIGN_ERROR);
            return;
        }

        filterChain.doFilter(requestWrapper, response);
    }

    private void sendErrorResponse(HttpServletResponse response, BizSignCodeEnum codeEnum) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpStatus.FORBIDDEN.value());
        JsonUtil.writeValue(response.getWriter(), Result.init(codeEnum.value(), null, codeEnum.desc()));
    }

}

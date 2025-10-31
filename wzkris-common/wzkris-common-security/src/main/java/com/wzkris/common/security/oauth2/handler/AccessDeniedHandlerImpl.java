package com.wzkris.common.security.oauth2.handler;

import com.wzkris.common.core.enums.BizBaseCode;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.I18nUtil;
import com.wzkris.common.core.utils.JsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 权限不足处理类
 */
@Slf4j
public final class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        JsonUtil.writeValue(response.getWriter(), Result.init(BizBaseCode.ACCESS_DENIED.value(), null,
                I18nUtil.message("forbidden.accessDenied.permissionDenied")));
    }

}

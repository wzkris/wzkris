package com.wzkris.gateway.filter;

import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.gateway.properties.XssProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * XSS 过滤（Gateway MVC HandlerFilterFunction 实现）。
 * 对 POST/PUT 等 JSON 请求体做 HTML 标签剥离。
 *
 * @author wzkris
 */
@Component
@RequiredArgsConstructor
public class XssFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    public static final String RE_HTML_MARK = "(<[^<]*?>)|(<[\\s]*?/[^<]*?>)|(<[^<]*?/[\\s]*?>)";

    private final XssProperties xss;

    private static String readRequestBody(ServerRequest request) throws IOException {
        try (var reader = new InputStreamReader(
                request.servletRequest().getInputStream(), StandardCharsets.UTF_8)) {
            return new java.io.BufferedReader(reader).lines().collect(Collectors.joining("\n"));
        }
    }

    @Override
    public ServerResponse filter(ServerRequest request, HandlerFunction<ServerResponse> next) throws Exception {
        if (!Boolean.TRUE.equals(xss.getEnabled())) {
            return next.handle(request);
        }
        if (request.method() == HttpMethod.GET || request.method() == HttpMethod.DELETE) {
            return next.handle(request);
        }
        MediaType contentType = request.headers().contentType().orElse(null);
        if (contentType == null || !contentType.includes(MediaType.APPLICATION_JSON)) {
            return next.handle(request);
        }
        String path = request.path();
        if (CollectionUtils.isNotEmpty(xss.getExcludeUrls()) && xss.getExcludeUrls().contains(path)) {
            return next.handle(request);
        }

        String bodyStr = readRequestBody(request);
        if (StringUtil.isBlank(bodyStr)) {
            return next.handle(request);
        }

        String sanitized = bodyStr.replaceAll(RE_HTML_MARK, "");
        ServerRequest sanitizedRequest = ServerRequest.from(request).body(sanitized).build();
        return next.handle(sanitizedRequest);
    }

}

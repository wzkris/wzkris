package com.wzkris.system.websocket;

import com.wzkris.auth.feign.token.TokenFeign;
import com.wzkris.auth.feign.token.req.TokenReq;
import com.wzkris.auth.feign.token.resp.TokenResponse;
import com.wzkris.common.core.domain.CorePrincipal;
import com.wzkris.common.core.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * 握手认证
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHandShakeInterceptor implements HandshakeInterceptor {

    final String token = "token";

    private final TokenFeign tokenFeign;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        MultiValueMap<String, String> queryParams =
                UriComponentsBuilder.fromUri(request.getURI())
                        .build().getQueryParams();
        if (StringUtil.isBlank(queryParams.get(token).get(0))) {
            return false;
        }
        TokenResponse tokenResponse = tokenFeign.validateUser(new TokenReq(queryParams.get(token).get(0)));
        if (!tokenResponse.isSuccess()) {
            log.info("websocket握手token校验失败, response：{}", tokenResponse);
            return false;
        }
        attributes.put(CorePrincipal.class.getName(), tokenResponse.getPrincipal());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }

}

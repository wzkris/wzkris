package com.wzkris.gateway.controller;

import com.wzkris.common.core.exception.BaseException;
import com.wzkris.gateway.domain.StatisticsKey;
import com.wzkris.gateway.domain.req.PageViewReq;
import com.wzkris.gateway.service.StatisticsService;
import com.wzkris.gateway.service.TokenExtractionService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 埋点上报控制器
 */
@Slf4j
@RestController
@RequestMapping("/track")
@RequiredArgsConstructor
@PermitAll
public class TrackController {

    private final StatisticsService statisticsService;

    private final TokenExtractionService tokenExtractionService;

    /**
     * pageview 上报
     */
    @PostMapping("/pageview")
    public Mono<ResponseEntity<Object>> recordPageview(
            @RequestBody PageViewReq request,
            ServerWebExchange exchange) {

        return tokenExtractionService.getCurrentPrincipal(exchange.getRequest())
                .flatMap(principal -> {
                    recordPageview(principal.getType(), principal.getId(), request);
                    return Mono.just(ResponseEntity.noContent().build());
                })
                .onErrorResume(BaseException.class, rpcException -> {
                    ServerHttpResponse exchangeResponse = exchange.getResponse();
                    exchangeResponse.setRawStatusCode(rpcException.getHttpStatusCode());
                    return Mono.just(ResponseEntity.noContent().build());
                })
                .onErrorResume(throwable -> {
                    ServerHttpResponse exchangeResponse = exchange.getResponse();
                    exchangeResponse.setRawStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return Mono.just(ResponseEntity.noContent().build());
                });
    }

    private void recordPageview(String authType, Long userId, PageViewReq request) {
        LocalDateTime now = LocalDateTime.now();
        String dateStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String hourStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH"));

        StatisticsKey key = StatisticsKey.builder()
                .authType(authType)
                .userId(userId)
                .path(request.getView())
                .date(dateStr)
                .hour(hourStr)
                .build();

        statisticsService.recordUvPv(key);
    }

}



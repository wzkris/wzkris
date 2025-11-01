package com.wzkris.gateway.controller;

import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.enums.AuthType;
import com.wzkris.common.core.model.MyPrincipal;
import com.wzkris.gateway.domain.StatisticsKey;
import com.wzkris.gateway.domain.req.PageViewReq;
import com.wzkris.gateway.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
public class TrackController {

    private final StatisticsService statisticsService;

    /**
     * pageview 上报
     */
    @PostMapping("/pageview")
    public Mono<ResponseEntity<Object>> recordPageview(
            @RequestBody PageViewReq request,
            ServerWebExchange exchange) {

        return exchange.getPrincipal()
                .map(p -> (MyPrincipal) p)
                .flatMap(principal -> {
                    AuthType authType = principal.getType();
                    Long userId = principal.getId();

                    recordPageview(authType, userId, request);
                    return Mono.just(ResponseEntity.noContent().build());
                })
                .switchIfEmpty(Mono.defer(() -> {
                    recordPageview(AuthType.ANONYMOUS, SecurityConstants.DEFAULT_USER_ID, request);
                    return Mono.just(ResponseEntity.noContent().build());
                }));
    }

    private void recordPageview(AuthType authType, Long userId, PageViewReq request) {
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



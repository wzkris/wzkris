package com.wzkris.gateway.controller;

import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.gateway.domain.StatisticsKey;
import com.wzkris.gateway.domain.request.PageViewRequest;
import com.wzkris.gateway.service.StatisticsService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * pageview 上报
     */
    @PostMapping("/pageview")
    public ResponseEntity<Object> recordPageview(@RequestBody PageViewRequest request) {
        try {
            if (SecurityUtil.isAuth()) {
                BaseLoginUser loginUser = SecurityUtil.getLoginUser();
                recordPageview(loginUser.getAuthType().getValue(), loginUser.getUid(), request);
            }
        } catch (Exception e) {
            log.warn("页面访问统计失败: {}", e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    private void recordPageview(String authType, Long userId, PageViewRequest request) {
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


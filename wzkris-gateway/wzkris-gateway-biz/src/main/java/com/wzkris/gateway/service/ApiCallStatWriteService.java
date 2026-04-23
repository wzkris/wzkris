package com.wzkris.gateway.service;

import com.wzkris.gateway.domain.ApiCallEventDO;
import com.wzkris.gateway.domain.ApiCallStatKey;
import com.wzkris.gateway.repository.ApiCallRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * API统计写入服务
 *
 * @author wzkris
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiCallStatWriteService {

    private final ApiCallRepository apiCallRepository;

    public void recordApiCall(ApiCallStatKey key, boolean success) {
        try {
            apiCallRepository.recordApiCallEvent(ApiCallEventDO.builder()
                    .timestamp(LocalDateTime.now())
                    .authType(key.getAuthType())
                    .path(key.getPath())
                    .method(key.getMethod())
                    .userId(key.getUserId())
                    .success(success)
                    .statusCode(key.getStatusCode() == null ? 0 : key.getStatusCode())
                    .costMs(key.getCostMs() == null ? 0L : Math.max(0L, key.getCostMs()))
                    .build());
        } catch (Exception e) {
            log.error("API调用量统计异常: {}", e.getMessage(), e);
        }
    }

}

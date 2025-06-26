package com.wzkris.system.controller;

import com.wzkris.common.security.utils.SystemUserUtil;
import com.wzkris.common.web.utils.SseUtil;
import com.wzkris.system.domain.dto.SimpleMessageDTO;
import com.wzkris.system.listener.event.PublishMessageEvent;
import com.wzkris.system.utils.GlobalSseUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "SSE端点")
@Slf4j
@RestController
@RequestMapping("/sse_endpoint")
@RequiredArgsConstructor
public class SseEndpointController {

    @GetMapping(headers = "connect")
    public SseEmitter connect() {
        return SseUtil.connect(SystemUserUtil.getUserId());
    }

    @GetMapping(headers = "disconnect")
    public void disconnect() {
        GlobalSseUtil.disconnect(SystemUserUtil.getUserId());
    }

    @Scheduled(cron = "*/3 * * * * *") // 模拟
    public void cron() {
        GlobalSseUtil.publish(new PublishMessageEvent(null, new SimpleMessageDTO("重要通知", "1", "项目难度很大，考虑清楚技术选型")));
    }

}

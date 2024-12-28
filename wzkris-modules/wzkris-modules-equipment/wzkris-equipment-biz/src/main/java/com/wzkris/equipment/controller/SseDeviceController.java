package com.wzkris.equipment.controller;

import cn.hutool.core.util.RandomUtil;
import com.wzkris.common.security.utils.LoginUserUtil;
import com.wzkris.equipment.utils.SseUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * (sse)设备数据推送
 *
 * @author wzkris
 * @since 2024-12-03 09:02:00
 */
@Tag(name = "SSE端点")
@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
public class SseDeviceController {

    @GetMapping("/connect")
    public SseEmitter createConnect() {
        return SseUtil.connect(String.valueOf(LoginUserUtil.getUserId()));
    }

    @GetMapping("/disconnect")
    public void disconnect() {
        SseUtil.disconnect(String.valueOf(LoginUserUtil.getUserId()));
    }

    @Scheduled(cron = "*/3 * * * * *")// 模拟
    public void cron() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("signal", RandomUtil.randomInt(0, 28));
        map.put("temperature", RandomUtil.randomInt(-15, 40));
        SseUtil.sendAll(map);
    }

}

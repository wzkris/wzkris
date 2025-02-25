package com.wzkris.system.controller;

import com.wzkris.common.security.utils.LoginUserUtil;
import com.wzkris.common.web.utils.SseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 系统推送消息
 *
 * @author wzkris
 */
@Tag(name = "系统推送消息")
@RestController
@RequestMapping("/system_push")
@RequiredArgsConstructor
public class SystemPushController {

    @Operation(summary = "打开连接")
    @GetMapping("/connect")
    public SseEmitter createConnect() {
        return SseUtil.connect(LoginUserUtil.getUserId());
    }

    @Operation(summary = "断开连接")
    @GetMapping("/disconnect")
    public void disconnect() {
        SseUtil.disconnect(LoginUserUtil.getUserId());
    }

}

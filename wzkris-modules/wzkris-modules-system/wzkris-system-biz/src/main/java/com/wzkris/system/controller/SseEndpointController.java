package com.wzkris.system.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.web.utils.UserAgentUtil;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.security.utils.SystemUserUtil;
import com.wzkris.system.utils.GlobalSseUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Tag(name = "SSE端点")
@Slf4j
@RestController
@RequestMapping("/sse_endpoint")
@RequiredArgsConstructor
public class SseEndpointController extends BaseController {

    @GetMapping(headers = "connectors")
    public Result<List<GlobalSseUtil.Connector>> connectors(@RequestParam(value = PAGE_NUM, defaultValue = "1") int pageNum,
                                                            @RequestParam(value = PAGE_SIZE, defaultValue = "10") int pageSize) {
        List<GlobalSseUtil.Connector> connectors = GlobalSseUtil.listConnectors(pageNum, pageSize);
        return ok(connectors);
    }

    @GetMapping(headers = "connect")
    public SseEmitter connect(HttpServletRequest request,
                              @RequestHeader(value = "User-Agent") String userAgentStr) {
        UserAgent.ImmutableUserAgent userAgent = UserAgentUtil.INSTANCE.parse(userAgentStr);
        return GlobalSseUtil.connect(SystemUserUtil.getUserId(), 10_000,
                userAgent.getValue(UserAgent.AGENT_NAME), userAgent.getValue(ServletUtil.getClientIP(request)));
    }

    @GetMapping(headers = "disconnect")
    public void disconnect() {
        GlobalSseUtil.disconnect(SystemUserUtil.getUserId());
    }

}

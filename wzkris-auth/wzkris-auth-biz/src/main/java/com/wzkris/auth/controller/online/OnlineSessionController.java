package com.wzkris.auth.controller.online;

import com.wzkris.auth.api.online.OnlineSessionApi;
import com.wzkris.auth.api.online.request.SidRequest;
import com.wzkris.auth.api.online.response.OnlineSessionResponse;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.security.annotation.CheckPerms;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Tag(name = "在线会话")
@Slf4j
@RestController
@RequestMapping("/online-session")
@RequiredArgsConstructor
@CheckPerms
public class OnlineSessionController {

    private final OnlineSessionApi onlineSessionApi;

    @Operation(summary = "在线会话")
    @GetMapping("/query-list")
    public Result<Collection<OnlineSessionResponse>> queryList() {
        return onlineSessionApi.queryList();
    }

    @Operation(summary = "踢出会话")
    @PostMapping("/kickout")
    public Result<Void> kickout(@RequestBody SidRequest request) {
        return onlineSessionApi.kickout(request);
    }

}

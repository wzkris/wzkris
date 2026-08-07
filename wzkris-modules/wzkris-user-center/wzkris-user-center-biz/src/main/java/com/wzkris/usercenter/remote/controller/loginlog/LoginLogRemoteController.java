package com.wzkris.usercenter.remote.controller.loginlog;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.loginlog.LoginLogRemoteApi;
import com.wzkris.usercenter.remote.api.loginlog.request.LoginLogEventRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "登录日志")
@Validated
@RestController
@RequestMapping("/login-log-remote")
@RequiredArgsConstructor
public class LoginLogRemoteController {

    private final LoginLogRemoteApi loginLogRemoteApi;

    @Operation(summary = "保存登录日志")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody @NotEmpty List<LoginLogEventRequest> requestList) {
        return loginLogRemoteApi.save(requestList);
    }

}

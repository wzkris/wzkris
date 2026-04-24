package com.wzkris.system.remote.controller.loginlog;

import com.wzkris.common.core.model.Result;
import com.wzkris.system.remote.api.loginlog.LoginLogRemoteApi;
import com.wzkris.system.remote.api.loginlog.request.LoginLogBatchSaveRequest;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/login-log-remote")
@RequiredArgsConstructor
public class LoginLogRemoteController {

    private final LoginLogRemoteApi loginLogRemoteApi;

    /**
     * 批量保存登录日志
     */
    @PostMapping("/save")
    public Result<Void> save(@RequestBody @Valid LoginLogBatchSaveRequest request) {
        return loginLogRemoteApi.save(request.getLoginLogEventRequests());
    }

}





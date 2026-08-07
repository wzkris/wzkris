package com.wzkris.usercenter.remote.controller.operatelog;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.operatelog.OperateLogRemoteApi;
import com.wzkris.usercenter.remote.api.operatelog.request.OperateLogEventRequest;
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

@Tag(name = "操作日志")
@Validated
@RestController
@RequestMapping("/operate-log-remote")
@RequiredArgsConstructor
public class OperateLogRemoteController {

    private final OperateLogRemoteApi operateLogRemoteApi;

    @Operation(summary = "保存操作日志")
    @PostMapping("/save")
    public Result<Void> save(@RequestBody @NotEmpty List<OperateLogEventRequest> requestList) {
        return operateLogRemoteApi.save(requestList);
    }

}

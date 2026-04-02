package com.wzkris.system.remote.controller.operatelog;

import com.wzkris.common.core.model.Result;
import com.wzkris.system.remote.api.operatelog.OperateLogRemoteApi;
import com.wzkris.system.remote.api.operatelog.request.OperateLogEventRequest;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/operate-log-remote")
@RequiredArgsConstructor
public class OperateLogRemoteController {

    private final OperateLogRemoteApi operateLogRemoteApi;

    /**
     * 新增操作日志
     */
    @PostMapping("/save")
    public Result<Void> save(@RequestBody List<OperateLogEventRequest> requests) {
        return operateLogRemoteApi.save(requests);
    }

}





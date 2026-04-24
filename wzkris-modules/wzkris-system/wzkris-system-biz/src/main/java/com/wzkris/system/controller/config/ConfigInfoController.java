package com.wzkris.system.controller.config;

import com.wzkris.common.core.model.Result;
import com.wzkris.system.api.config.ConfigInfoApi;
import com.wzkris.system.request.config.ConfigMngQueryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "系统配置查询")
@RestController
@RequestMapping("/config-info")
@RequiredArgsConstructor
public class ConfigInfoController {

    private final ConfigInfoApi configInfoApi;

    @Operation(summary = "查询配置值")
    @GetMapping("/{configKey}")
    public Result<String> queryValue(ConfigMngQueryRequest request) {
        return configInfoApi.queryValue(request);
    }

}

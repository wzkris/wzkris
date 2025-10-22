package com.wzkris.message.controller.config;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.message.service.ConfigInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "系统配置查询")
@RestController
@RequestMapping("/config-info")
@RequiredArgsConstructor
public class ConfigInfoController extends BaseController {

    private final ConfigInfoService configInfoService;

    @Operation(summary = "查询配置值")
    @GetMapping("/{configKey}")
    public Result<String> queryValue(@PathVariable String configKey) {
        return ok(configInfoService.getValueByKey(configKey));
    }

}

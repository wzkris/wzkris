package com.wzkris.system.controller.config;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.system.api.config.ConfigMngApi;
import com.wzkris.system.api.config.request.ConfigMngPageRequest;
import com.wzkris.system.api.config.request.ConfigMngSaveRequest;
import com.wzkris.system.api.config.request.ConfigMngUpdateRequest;
import com.wzkris.system.api.config.response.ConfigInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "系统配置管理")
@Validated
@RestController
@RequestMapping("/config-manage")
@RequiredArgsConstructor
public class ConfigMngController {

    private final ConfigMngApi configMngApi;

    @Operation(summary = "分页")
    @GetMapping("/query-page")
    @CheckAdminPerms("system-mod:config-mng:page")
    public Result<Page<ConfigInfoResponse>> queryPage(@ParameterObject ConfigMngPageRequest request) {
        return configMngApi.queryPage(request);
    }

    @Operation(summary = "详情")
    @GetMapping("/query-info/{id}")
    @CheckAdminPerms("system-mod:config-mng:page")
    public Result<ConfigInfoResponse> queryInfo(@ParameterObject IdRequest request) {
        return configMngApi.queryInfo(request);
    }

    @Operation(summary = "添加参数")
    @OperateLog(title = "参数管理", subTitle = "添加参数", type = OperateTypeEnum.INSERT)
    @PostMapping("/save")
    @CheckAdminPerms("system-mod:config-mng:add")
    public Result<Void> save(@RequestBody ConfigMngSaveRequest request) {
        return configMngApi.save(request);
    }

    @Operation(summary = "修改参数")
    @OperateLog(title = "参数管理", subTitle = "修改参数", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update")
    @CheckAdminPerms("system-mod:config-mng:edit")
    public Result<Void> update(@RequestBody ConfigMngUpdateRequest request) {
        return configMngApi.update(request);
    }

    @Operation(summary = "删除参数")
    @OperateLog(title = "参数管理", subTitle = "删除参数", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckAdminPerms("system-mod:config-mng:remove")
    public Result<Void> remove(@RequestBody @Valid IdRequest request) {
        return configMngApi.remove(request);
    }

    @Operation(summary = "刷新参数缓存")
    @PostMapping("/refresh-cache")
    @CheckAdminPerms("system-mod:config-mng:remove")
    public Result<Void> refreshCache() {
        return configMngApi.refreshCache();
    }

}


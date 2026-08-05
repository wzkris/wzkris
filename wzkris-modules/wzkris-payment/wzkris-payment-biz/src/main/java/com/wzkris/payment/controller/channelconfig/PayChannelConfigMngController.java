package com.wzkris.payment.controller.channelconfig;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.payment.api.channelconfig.PayChannelConfigMngApi;
import com.wzkris.payment.api.channelconfig.request.PayChannelConfigMngPageRequest;
import com.wzkris.payment.api.channelconfig.request.PayChannelConfigSaveRequest;
import com.wzkris.payment.api.channelconfig.request.PayChannelConfigUpdateRequest;
import com.wzkris.payment.api.channelconfig.response.PayChannelConfigResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 渠道配置管理（后台）
 *
 * @author wzkris
 */
@Tag(name = "渠道配置管理")
@Validated
@RestController
@RequestMapping("/pay-channel-config-manage")
@RequiredArgsConstructor
public class PayChannelConfigMngController {

    private static final String PERM_PREFIX = "pay-mod:channel-config:";

    private final PayChannelConfigMngApi configMngApi;

    @Operation(summary = "渠道配置分页")
    @GetMapping("/query-page")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "page")
    public Result<Page<PayChannelConfigResponse>> queryPage(@ParameterObject PayChannelConfigMngPageRequest request) {
        return configMngApi.queryPage(request);
    }

    @Operation(summary = "渠道配置详情")
    @GetMapping("/query-info")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "page")
    public Result<PayChannelConfigResponse> queryInfo(@ParameterObject IdRequest request) {
        return configMngApi.queryInfo(request);
    }

    @Operation(summary = "新增渠道配置")
    @OperateLog(title = "渠道配置管理", subTitle = "新增", type = OperateTypeEnum.INSERT)
    @PostMapping("/save")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "add")
    public Result<Void> save(@Validated @RequestBody PayChannelConfigSaveRequest request) {
        return configMngApi.save(request);
    }

    @Operation(summary = "修改渠道配置")
    @OperateLog(title = "渠道配置管理", subTitle = "修改", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "edit")
    public Result<Void> update(@Validated @RequestBody PayChannelConfigUpdateRequest request) {
        return configMngApi.update(request);
    }

    @Operation(summary = "删除渠道配置")
    @OperateLog(title = "渠道配置管理", subTitle = "删除", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "remove")
    public Result<Void> remove(@RequestBody IdListRequest request) {
        return configMngApi.remove(request);
    }
}

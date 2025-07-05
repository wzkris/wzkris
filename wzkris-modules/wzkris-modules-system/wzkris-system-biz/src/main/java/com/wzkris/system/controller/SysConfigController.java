package com.wzkris.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckSystemPerms;
import com.wzkris.system.domain.SysConfig;
import com.wzkris.system.domain.req.SysConfigQueryReq;
import com.wzkris.system.domain.req.SysConfigReq;
import com.wzkris.system.mapper.SysConfigMapper;
import com.wzkris.system.service.SysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 参数配置 信息操作处理
 *
 * @author wzkris
 */
@Tag(name = "参数管理")
@RestController
@PreAuthorize("@su.isSuperTenant()") // 只允许超级租户访问
@RequestMapping("/sys_config")
@RequiredArgsConstructor
public class SysConfigController extends BaseController {

    private final SysConfigMapper configMapper;

    private final SysConfigService configService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @CheckSystemPerms("sys_config:list")
    public Result<Page<SysConfig>> list(SysConfigQueryReq queryReq) {
        startPage();
        List<SysConfig> list = configMapper.selectList(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<SysConfig> buildQueryWrapper(SysConfigQueryReq queryReq) {
        return new LambdaQueryWrapper<SysConfig>()
                .like(StringUtil.isNotEmpty(queryReq.getConfigKey()), SysConfig::getConfigKey, queryReq.getConfigKey())
                .like(
                        StringUtil.isNotEmpty(queryReq.getConfigName()),
                        SysConfig::getConfigName,
                        queryReq.getConfigName())
                .like(
                        StringUtil.isNotEmpty(queryReq.getConfigType()),
                        SysConfig::getConfigType,
                        queryReq.getConfigType())
                .orderByDesc(SysConfig::getConfigId);
    }

    @Operation(summary = "详情")
    @GetMapping("/{configId}")
    @CheckSystemPerms("sys_config:query")
    public Result<SysConfig> getInfo(@PathVariable Long configId) {
        return ok(configMapper.selectById(configId));
    }

    @Operation(summary = "添加参数")
    @OperateLog(title = "参数管理", subTitle = "添加参数", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckSystemPerms("sys_config:add")
    public Result<Void> add(@Validated @RequestBody SysConfigReq req) {
        if (configService.checkUsedByConfigKey(null, req.getConfigKey())) {
            return err412("新增参数'" + req.getConfigName() + "'失败，参数键名已存在");
        }
        return toRes(configService.insertConfig(BeanUtil.convert(req, SysConfig.class)));
    }

    @Operation(summary = "修改参数")
    @OperateLog(title = "参数管理", subTitle = "修改参数", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckSystemPerms("sys_config:edit")
    public Result<Void> edit(@Validated @RequestBody SysConfigReq req) {
        if (configService.checkUsedByConfigKey(req.getConfigId(), req.getConfigKey())) {
            return err412("修改参数'" + req.getConfigName() + "'失败，参数键名已存在");
        }
        return toRes(configService.updateConfig(BeanUtil.convert(req, SysConfig.class)));
    }

    @Operation(summary = "删除参数")
    @OperateLog(title = "参数管理", subTitle = "删除参数", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckSystemPerms("sys_config:remove")
    public Result<Void> remove(@RequestBody Long configId) {
        SysConfig config = configMapper.selectById(configId);
        if (StringUtil.equals(CommonConstants.YES, config.getConfigType())) {
            return err412(String.format("内置参数'%s'不能删除", config.getConfigKey()));
        }
        return toRes(configService.deleteById(configId));
    }

    @Operation(summary = "刷新参数缓存")
    @PostMapping("/refresh_cache")
    @CheckSystemPerms("sys_config:remove")
    public Result<Void> refreshCache() {
        configService.loadingConfigCache();
        return ok();
    }

}

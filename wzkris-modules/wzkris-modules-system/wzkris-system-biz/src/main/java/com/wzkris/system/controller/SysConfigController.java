package com.wzkris.system.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.page.Page;
import com.wzkris.system.domain.SysConfig;
import com.wzkris.system.mapper.SysConfigMapper;
import com.wzkris.system.service.SysConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 参数配置 信息操作处理
 *
 * @author wzkris
 */
@Tag(name = "系统配置")
@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
public class SysConfigController extends BaseController {
    private final SysConfigMapper sysConfigMapper;
    private final SysConfigService sysConfigService;

    /**
     * 获取参数配置列表
     */
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('config:list')")
    public Result<Page<SysConfig>> list(SysConfig config) {
        startPage();
        List<SysConfig> list = sysConfigService.list(config);
        return getDataTable(list);
    }

    /**
     * 根据参数编号获取详细信息
     */
    @GetMapping("/{configId}")
    public Result<?> getInfo(@PathVariable Long configId) {
        return success(sysConfigMapper.selectById(configId));
    }

    /**
     * 根据参数键名查询参数值
     */
    @GetMapping("/configKey/{configKey}")
    public Result<String> getConfigKey(@PathVariable String configKey) {
        return Result.success(sysConfigService.getConfigValueByKey(configKey));
    }

    /**
     * 新增参数配置
     */
    @OperateLog(title = "参数管理", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('config:add')")
    public Result<?> add(@Validated @RequestBody SysConfig config) {
        if (sysConfigService.checkConfigKeyUnique(config)) {
            return fail("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        return toRes(sysConfigService.insertConfig(config));
    }

    /**
     * 修改参数配置
     */
    @OperateLog(title = "参数管理", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('config:edit')")
    public Result<?> edit(@Validated @RequestBody SysConfig config) {
        if (sysConfigService.checkConfigKeyUnique(config)) {
            return fail("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        return toRes(sysConfigService.updateConfig(config));
    }

    /**
     * 删除参数配置
     */
    @OperateLog(title = "参数管理", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('config:remove')")
    public Result<?> remove(@RequestBody Long[] configIds) {
        List<Long> list = Arrays.asList(configIds);
        sysConfigService.deleteConfigByIds(list);
        return success();
    }

    /**
     * 刷新参数缓存
     */
    @OperateLog(title = "参数管理", operateType = OperateType.DELETE)
    @PostMapping("/refresh_cache")
    @PreAuthorize("@ps.hasPerms('config:remove')")
    public Result<?> refreshCache() {
        sysConfigService.resetConfigCache();
        return success();
    }
}

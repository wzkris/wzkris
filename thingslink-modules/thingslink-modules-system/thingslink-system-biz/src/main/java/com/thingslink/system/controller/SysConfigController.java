package com.thingslink.system.controller;

import com.thingslink.common.core.domain.Result;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.OperateType;
import com.thingslink.common.orm.model.BaseController;
import com.thingslink.common.orm.page.Page;
import com.thingslink.system.domain.SysConfig;
import com.thingslink.system.mapper.SysConfigMapper;
import com.thingslink.system.service.SysConfigService;
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
    @PreAuthorize("hasAuthority('config:list')")
    public Result<Page<SysConfig>> list(SysConfig config) {
        startPage();
        List<SysConfig> list = sysConfigService.list(config);
        return getDataTable(list);
    }

    /**
     * 根据参数编号获取详细信息
     */
    @GetMapping(value = "/{configId}")
    public Result<?> getInfo(@PathVariable Long configId) {
        return success(sysConfigMapper.selectById(configId));
    }

    /**
     * 根据参数键名查询参数值
     */
    @GetMapping(value = "/configKey/{configKey}")
    public Result<String> getConfigKey(@PathVariable String configKey) {
        return Result.success(sysConfigService.getConfigValueByKey(configKey));
    }

    /**
     * 新增参数配置
     */
    @OperateLog(title = "参数管理", operateType = OperateType.INSERT)
    @PostMapping
    @PreAuthorize("hasAuthority('config:add')")
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
    @PutMapping
    @PreAuthorize("hasAuthority('config:edit')")
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
    @DeleteMapping("{configIds}")
    @PreAuthorize("hasAuthority('config:remove')")
    public Result<?> remove(@PathVariable Long[] configIds) {
        sysConfigService.deleteConfigByIds(configIds);
        return success();
    }

    /**
     * 刷新参数缓存
     */
    @OperateLog(title = "参数管理", operateType = OperateType.DELETE)
    @DeleteMapping("refreshCache")
    @PreAuthorize("hasAuthority('config:remove')")
    public Result<?> refreshCache() {
        sysConfigService.resetConfigCache();
        return success();
    }
}

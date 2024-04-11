package com.thingslink.system.controller;

import com.thingslink.common.core.domain.Result;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.BusinessType;
import com.thingslink.common.orm.page.Page;
import com.thingslink.common.web.controller.BaseController;
import com.thingslink.system.domain.Config;
import com.thingslink.system.mapper.ConfigMapper;
import com.thingslink.system.service.ConfigService;
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
public class ConfigController extends BaseController {
    private final ConfigMapper configMapper;
    private final ConfigService configService;

    /**
     * 获取参数配置列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('config:list')")
    public Result<Page<Config>> list(Config config) {
        startPage();
        List<Config> list = configService.list(config);
        return getDataTable(list);
    }

    /**
     * 根据参数编号获取详细信息
     */
    @GetMapping(value = "/{configId}")
    public Result<?> getInfo(@PathVariable Long configId) {
        return success(configMapper.selectById(configId));
    }

    /**
     * 根据参数键名查询参数值
     */
    @GetMapping(value = "/configKey/{configKey}")
    public Result<String> getConfigKey(@PathVariable String configKey) {
        return Result.success(configService.getConfigValueByKey(configKey));
    }

    /**
     * 新增参数配置
     */
    @OperateLog(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping
    @PreAuthorize("hasAuthority('config:add')")
    public Result<?> add(@Validated @RequestBody Config config) {
        if (configService.checkConfigKeyUnique(config)) {
            return fail("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        return toRes(configService.insertConfig(config));
    }

    /**
     * 修改参数配置
     */
    @OperateLog(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @PreAuthorize("hasAuthority('config:edit')")
    public Result<?> edit(@Validated @RequestBody Config config) {
        if (configService.checkConfigKeyUnique(config)) {
            return fail("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        return toRes(configService.updateConfig(config));
    }

    /**
     * 删除参数配置
     */
    @OperateLog(title = "参数管理", businessType = BusinessType.DELETE)
    @DeleteMapping("{configIds}")
    @PreAuthorize("hasAuthority('config:remove')")
    public Result<?> remove(@PathVariable Long[] configIds) {
        configService.deleteConfigByIds(configIds);
        return success();
    }

    /**
     * 刷新参数缓存
     */
    @OperateLog(title = "参数管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("refreshCache")
    @PreAuthorize("hasAuthority('config:remove')")
    public Result<?> refreshCache() {
        configService.resetConfigCache();
        return success();
    }
}

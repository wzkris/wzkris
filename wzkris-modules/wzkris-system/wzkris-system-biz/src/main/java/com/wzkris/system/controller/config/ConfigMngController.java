package com.wzkris.system.controller.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.system.domain.ConfigInfoDO;
import com.wzkris.system.domain.req.config.ConfigMngQueryReq;
import com.wzkris.system.domain.req.config.ConfigMngReq;
import com.wzkris.system.mapper.ConfigInfoMapper;
import com.wzkris.system.service.ConfigInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 参数配置 信息操作处理
 *
 * @author wzkris
 */
@Tag(name = "系统配置管理")
@Validated
@RestController
@RequestMapping("/config-manage")
@RequiredArgsConstructor
public class ConfigMngController extends BaseController {

    private final ConfigInfoMapper configInfoMapper;

    private final ConfigInfoService configInfoService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    @CheckAdminPerms("system-mod:config-mng:page")
    public Result<Page<ConfigInfoDO>> page(ConfigMngQueryReq queryReq) {
        startPage();
        List<ConfigInfoDO> list = configInfoMapper.selectList(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<ConfigInfoDO> buildQueryWrapper(ConfigMngQueryReq queryReq) {
        return new LambdaQueryWrapper<ConfigInfoDO>()
                .like(StringUtil.isNotEmpty(queryReq.getConfigKey()), ConfigInfoDO::getConfigKey, queryReq.getConfigKey())
                .like(
                        StringUtil.isNotEmpty(queryReq.getConfigName()),
                        ConfigInfoDO::getConfigName,
                        queryReq.getConfigName())
                .like(
                        StringUtil.isNotEmpty(queryReq.getConfigType()),
                        ConfigInfoDO::getConfigType,
                        queryReq.getConfigType())
                .orderByDesc(ConfigInfoDO::getConfigId);
    }

    @Operation(summary = "详情")
    @GetMapping("/{configId}")
    @CheckAdminPerms("system-mod:config-mng:page")
    public Result<ConfigInfoDO> getInfo(@PathVariable Long configId) {
        return ok(configInfoMapper.selectById(configId));
    }

    @Operation(summary = "添加参数")
    @OperateLog(title = "参数管理", subTitle = "添加参数", type = OperateTypeEnum.INSERT)
    @PostMapping("/add")
    @CheckAdminPerms("system-mod:config-mng:add")
    public Result<Void> add(@RequestBody ConfigMngReq req) {
        if (configInfoService.checkUsedByConfigKey(null, req.getConfigKey())) {
            return err40000("新增参数'" + req.getConfigName() + "'失败，参数键名已存在");
        }
        return toRes(configInfoService.insertConfig(BeanUtil.convert(req, ConfigInfoDO.class)));
    }

    @Operation(summary = "修改参数")
    @OperateLog(title = "参数管理", subTitle = "修改参数", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit")
    @CheckAdminPerms("system-mod:config-mng:edit")
    public Result<Void> edit(@RequestBody ConfigMngReq req) {
        if (configInfoService.checkUsedByConfigKey(req.getConfigId(), req.getConfigKey())) {
            return err40000("修改参数'" + req.getConfigName() + "'失败，参数键名已存在");
        }
        return toRes(configInfoService.updateConfig(BeanUtil.convert(req, ConfigInfoDO.class)));
    }

    @Operation(summary = "删除参数")
    @OperateLog(title = "参数管理", subTitle = "删除参数", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckAdminPerms("system-mod:config-mng:remove")
    public Result<Void> remove(@RequestBody Long configId) {
        ConfigInfoDO config = configInfoMapper.selectById(configId);
        if (config.getBuiltIn()) {
            return err40000(String.format("内置参数'%s'不能删除", config.getConfigKey()));
        }
        return toRes(configInfoService.deleteById(configId));
    }

    @Operation(summary = "刷新参数缓存")
    @PostMapping("/refresh-cache")
    @CheckAdminPerms("system-mod:config-mng:remove")
    public Result<Void> refreshCache() {
        configInfoService.loadingConfigCache();
        return ok();
    }

}

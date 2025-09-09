package com.wzkris.system.controller.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckUserPerms;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.system.domain.ConfigInfoDO;
import com.wzkris.system.domain.req.config.ConfigManageQueryReq;
import com.wzkris.system.domain.req.config.ConfigManageReq;
import com.wzkris.system.mapper.ConfigInfoMapper;
import com.wzkris.system.service.ConfigInfoService;
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
@Tag(name = "系统配置管理")
@Validated
@RestController
@RequestMapping("/config-manage")
@PreAuthorize("@lu.isSuperTenant()")
@RequiredArgsConstructor
public class ConfigManageController extends BaseController {

    private final ConfigInfoMapper configInfoMapper;

    private final ConfigInfoService configInfoService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @CheckUserPerms("system-mod:config-mng:list")
    public Result<Page<ConfigInfoDO>> list(ConfigManageQueryReq queryReq) {
        startPage();
        List<ConfigInfoDO> list = configInfoMapper.selectList(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<ConfigInfoDO> buildQueryWrapper(ConfigManageQueryReq queryReq) {
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
    @CheckUserPerms("system-mod:config-mng:query")
    public Result<ConfigInfoDO> getInfo(@PathVariable Long configId) {
        return ok(configInfoMapper.selectById(configId));
    }

    @Operation(summary = "添加参数")
    @OperateLog(title = "参数管理", subTitle = "添加参数", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckUserPerms("system-mod:config-mng:add")
    public Result<Void> add(@RequestBody ConfigManageReq req) {
        if (configInfoService.checkUsedByConfigKey(null, req.getConfigKey())) {
            return err40000("新增参数'" + req.getConfigName() + "'失败，参数键名已存在");
        }
        return toRes(configInfoService.insertConfig(BeanUtil.convert(req, ConfigInfoDO.class)));
    }

    @Operation(summary = "修改参数")
    @OperateLog(title = "参数管理", subTitle = "修改参数", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckUserPerms("system-mod:config-mng:edit")
    public Result<Void> edit(@RequestBody ConfigManageReq req) {
        if (configInfoService.checkUsedByConfigKey(req.getConfigId(), req.getConfigKey())) {
            return err40000("修改参数'" + req.getConfigName() + "'失败，参数键名已存在");
        }
        return toRes(configInfoService.updateConfig(BeanUtil.convert(req, ConfigInfoDO.class)));
    }

    @Operation(summary = "删除参数")
    @OperateLog(title = "参数管理", subTitle = "删除参数", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckUserPerms("system-mod:config-mng:remove")
    public Result<Void> remove(@RequestBody Long configId) {
        ConfigInfoDO config = configInfoMapper.selectById(configId);
        if (StringUtil.equals(CommonConstants.YES, config.getConfigType())) {
            return err40000(String.format("内置参数'%s'不能删除", config.getConfigKey()));
        }
        return toRes(configInfoService.deleteById(configId));
    }

    @Operation(summary = "刷新参数缓存")
    @PostMapping("/refresh-cache")
    @CheckUserPerms("system-mod:config-mng:remove")
    public Result<Void> refreshCache() {
        configInfoService.loadingConfigCache();
        return ok();
    }

}

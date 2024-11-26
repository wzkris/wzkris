package com.wzkris.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.system.domain.GlobalDictData;
import com.wzkris.system.mapper.GlobalDictDataMapper;
import com.wzkris.system.service.GlobalDictDataService;
import com.wzkris.system.utils.DictCacheUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典信息
 *
 * @author wzkris
 */
@Tag(name = "字典数据")
@RestController
@RequestMapping("/dict/data")
@RequiredArgsConstructor
public class GlobalDictDataController extends BaseController {
    private final GlobalDictDataMapper dictDataMapper;
    private final GlobalDictDataService dictDataService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('dict:list')")
    public Result<Page<GlobalDictData>> list(GlobalDictData globalDictData) {
        startPage();
        LambdaQueryWrapper<GlobalDictData> lqw = new LambdaQueryWrapper<GlobalDictData>()
                .like(StringUtil.isNotBlank(globalDictData.getDictType()), GlobalDictData::getDictType, globalDictData.getDictType())
                .like(StringUtil.isNotBlank(globalDictData.getDictLabel()), GlobalDictData::getDictLabel, globalDictData.getDictLabel())
                .orderByAsc(GlobalDictData::getDictSort);
        return getDataTable(dictDataMapper.selectList(lqw));
    }

    @Operation(summary = "详情")
    @GetMapping("/{dictCode}")
    @PreAuthorize("@ps.hasPerms('dict:query')")
    public Result<GlobalDictData> getInfo(@PathVariable Long dictCode) {
        return ok(dictDataMapper.selectById(dictCode));
    }

    @Operation(summary = "类型查询字典数据")
    @GetMapping("/type/{dictType}")
    public Result<List<GlobalDictData>> dictType(@PathVariable String dictType) {
        List<GlobalDictData> dictCache = DictCacheUtil.getDictCache(dictType);
        if (dictCache != null) {
            return ok(dictCache);
        }
        return ok(dictDataMapper.listByType(dictType));
    }

    @Operation(summary = "新增字典")
    @OperateLog(title = "字典数据", subTitle = "新增字典", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('dict:add')")
    public Result<?> add(@Validated @RequestBody GlobalDictData dict) {
        dictDataService.insertDictData(dict);
        return ok();
    }

    @Operation(summary = "修改字典")
    @OperateLog(title = "字典数据", subTitle = "修改字典", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('dict:edit')")
    public Result<?> edit(@Validated @RequestBody GlobalDictData dict) {
        dictDataService.updateDictData(dict);
        return ok();
    }

    @Operation(summary = "删除字典")
    @OperateLog(title = "字典类型", subTitle = "删除字典", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('dict:remove')")
    public Result<?> remove(@RequestBody List<Long> dataIds) {
        dictDataService.deleteDictData(dataIds);
        return ok();
    }
}

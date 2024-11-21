package com.wzkris.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.system.domain.GlobalDictType;
import com.wzkris.system.mapper.GlobalDictTypeMapper;
import com.wzkris.system.service.GlobalDictTypeService;
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
@Tag(name = "字典类型")
@RestController
@RequestMapping("/dict/type")
@RequiredArgsConstructor
public class GlobalDictTypeController extends BaseController {
    private final GlobalDictTypeMapper dictTypeMapper;
    private final GlobalDictTypeService dictTypeService;


    @Operation(summary = "分页")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('dict:list')")
    public Result<Page<GlobalDictType>> list(GlobalDictType globalDictType) {
        startPage();
        LambdaQueryWrapper<GlobalDictType> lqw = new LambdaQueryWrapper<GlobalDictType>()
                .like(StringUtil.isNotBlank(globalDictType.getDictName()), GlobalDictType::getDictName, globalDictType.getDictName())
                .like(StringUtil.isNotBlank(globalDictType.getDictType()), GlobalDictType::getDictType, globalDictType.getDictType());
        return getDataTable(dictTypeMapper.selectList(lqw));
    }

    @Operation(summary = "详情")
    @GetMapping("/{dictId}")
    @PreAuthorize("@ps.hasPerms('dict:query')")
    public Result<GlobalDictType> getInfo(@PathVariable Long dictId) {
        return ok(dictTypeMapper.selectById(dictId));
    }

    @Operation(summary = "新增")
    @OperateLog(title = "字典类型", subTitle = "添加字典", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('dict:add')")
    public Result<Void> add(@Validated @RequestBody GlobalDictType dict) {
        if (dictTypeService.checkDictTypeUnique(dict)) {
            return fail("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dictTypeService.insertDictType(dict);
        return ok();
    }

    @Operation(summary = "修改")
    @OperateLog(title = "字典类型", subTitle = "修改字典", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('dict:edit')")
    public Result<Void> edit(@Validated @RequestBody GlobalDictType dict) {
        if (dictTypeService.checkDictTypeUnique(dict)) {
            return fail("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dictTypeService.updateDictType(dict);
        return ok();
    }

    @Operation(summary = "删除")
    @OperateLog(title = "字典类型", subTitle = "删除字典", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('dict:remove')")
    public Result<Void> remove(@RequestBody List<Long> typeIds) {
        if (dictTypeService.checkDictTypeUsed(typeIds)) {
            return fail("删除失败，该字典类型已被使用");
        }
        dictTypeService.deleteByIds(typeIds);
        return ok();
    }

    @Operation(summary = "刷新字典缓存")
    @PostMapping("/refreshCache")
    @PreAuthorize("@ps.hasPerms('dict:remove')")
    public Result<?> refreshCache() {
        DictCacheUtil.clearAll();
        dictTypeService.loadingDictCache();
        return ok();
    }

    @Operation(summary = "字典选择框")
    @GetMapping("/optionselect")
    public Result<List<GlobalDictType>> optionselect() {
        List<GlobalDictType> globalDictTypes = dictTypeMapper.selectList(null);
        return ok(globalDictTypes);
    }
}

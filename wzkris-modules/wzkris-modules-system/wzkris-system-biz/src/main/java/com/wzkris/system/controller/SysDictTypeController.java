package com.wzkris.system.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.page.Page;
import com.wzkris.system.domain.SysDictType;
import com.wzkris.system.mapper.SysDictTypeMapper;
import com.wzkris.system.service.SysDictTypeService;
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
public class SysDictTypeController extends BaseController {
    private final SysDictTypeMapper sysDictTypeMapper;
    private final SysDictTypeService dictTypeService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('dict:list')")
    public Result<Page<SysDictType>> list(SysDictType sysDictType) {
        startPage();
        List<SysDictType> list = dictTypeService.list(sysDictType);
        return getDataTable(list);
    }

    /**
     * 查询字典类型详细
     */
    @GetMapping("/{dictId}")
    @PreAuthorize("@ps.hasPerms('dict:query')")
    public Result<?> getInfo(@PathVariable Long dictId) {
        return ok(sysDictTypeMapper.selectById(dictId));
    }

    /**
     * 新增字典类型
     */
    @OperateLog(title = "字典类型", subTitle = "添加字典", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('dict:add')")
    public Result<?> add(@Validated @RequestBody SysDictType dict) {
        if (dictTypeService.checkDictTypeUnique(dict)) {
            return fail("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        return toRes(dictTypeService.insertDictType(dict));
    }

    /**
     * 修改字典类型
     */
    @OperateLog(title = "字典类型", subTitle = "修改字典", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('dict:edit')")
    public Result<?> edit(@Validated @RequestBody SysDictType dict) {
        if (dictTypeService.checkDictTypeUnique(dict)) {
            return fail("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        return toRes(dictTypeService.updateDictType(dict));
    }

    /**
     * 删除字典类型
     */
    @OperateLog(title = "字典类型", subTitle = "删除字典", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('dict:remove')")
    public Result<?> remove(@RequestBody Long[] dictIds) {
        dictTypeService.deleteDictTypeByIds(dictIds);
        return ok();
    }

    /**
     * 刷新字典缓存
     */
    @PostMapping("/refreshCache")
    @PreAuthorize("@ps.hasPerms('dict:remove')")
    public Result<?> refreshCache() {
        dictTypeService.resetDictCache();
        return ok();
    }

    /**
     * 获取字典选择框列表
     */
    @GetMapping("/optionselect")
    public Result<?> optionselect() {
        List<SysDictType> sysDictTypes = sysDictTypeMapper.selectList(null);
        return ok(sysDictTypes);
    }
}

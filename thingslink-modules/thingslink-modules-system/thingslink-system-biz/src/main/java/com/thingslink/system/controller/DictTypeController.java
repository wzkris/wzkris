package com.thingslink.system.controller;

import com.thingslink.common.core.domain.Result;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.BusinessType;
import com.thingslink.common.orm.page.Page;
import com.thingslink.common.web.controller.BaseController;
import com.thingslink.system.domain.DictType;
import com.thingslink.system.mapper.DictTypeMapper;
import com.thingslink.system.service.DictTypeService;
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
public class DictTypeController extends BaseController {
    private final DictTypeMapper dictTypeMapper;
    private final DictTypeService dictTypeService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('dict:list')")
    public Result<Page<DictType>> list(DictType dictType) {
        startPage();
        List<DictType> list = dictTypeService.list(dictType);
        return getDataTable(list);
    }

    /**
     * 查询字典类型详细
     */
    @GetMapping("/{dictId}")
    @PreAuthorize("hasAuthority('dict:query')")
    public Result<?> getInfo(@PathVariable Long dictId) {
        return success(dictTypeMapper.selectById(dictId));
    }

    /**
     * 新增字典类型
     */
    @OperateLog(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping
    @PreAuthorize("hasAuthority('dict:add')")
    public Result<?> add(@Validated @RequestBody DictType dict) {
        if (dictTypeService.checkDictTypeUnique(dict)) {
            return fail("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        return toRes(dictTypeService.insertDictType(dict));
    }

    /**
     * 修改字典类型
     */
    @OperateLog(title = "字典类型", businessType = BusinessType.UPDATE)
    @PutMapping
    @PreAuthorize("hasAuthority('dict:edit')")
    public Result<?> edit(@Validated @RequestBody DictType dict) {
        if (dictTypeService.checkDictTypeUnique(dict)) {
            return fail("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        return toRes(dictTypeService.updateDictType(dict));
    }

    /**
     * 删除字典类型
     */
    @OperateLog(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictIds}")
    @PreAuthorize("hasAuthority('dict:remove')")
    public Result<?> remove(@PathVariable Long[] dictIds) {
        dictTypeService.deleteDictTypeByIds(dictIds);
        return success();
    }

    /**
     * 刷新字典缓存
     */
    @OperateLog(title = "字典类型", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    @PreAuthorize("hasAuthority('dict:remove')")
    public Result<?> refreshCache() {
        dictTypeService.resetDictCache();
        return success();
    }

    /**
     * 获取字典选择框列表
     */
    @GetMapping("/optionselect")
    public Result<?> optionselect() {
        List<DictType> dictTypes = dictTypeMapper.selectList(null);
        return success(dictTypes);
    }
}

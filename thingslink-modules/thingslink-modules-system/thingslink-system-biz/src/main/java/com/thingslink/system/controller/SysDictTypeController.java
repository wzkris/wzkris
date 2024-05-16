package com.thingslink.system.controller;

import com.thingslink.common.core.domain.Result;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.OperateType;
import com.thingslink.common.orm.model.BaseController;
import com.thingslink.common.orm.page.Page;
import com.thingslink.system.domain.SysDictType;
import com.thingslink.system.mapper.SysDictTypeMapper;
import com.thingslink.system.service.SysDictTypeService;
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
    private final SysDictTypeService sysDictTypeService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('dict:list')")
    public Result<Page<SysDictType>> list(SysDictType sysDictType) {
        startPage();
        List<SysDictType> list = sysDictTypeService.list(sysDictType);
        return getDataTable(list);
    }

    /**
     * 查询字典类型详细
     */
    @GetMapping("/{dictId}")
    @PreAuthorize("hasAuthority('dict:query')")
    public Result<?> getInfo(@PathVariable Long dictId) {
        return success(sysDictTypeMapper.selectById(dictId));
    }

    /**
     * 新增字典类型
     */
    @OperateLog(title = "字典类型", operateType = OperateType.INSERT)
    @PostMapping
    @PreAuthorize("hasAuthority('dict:add')")
    public Result<?> add(@Validated @RequestBody SysDictType dict) {
        if (sysDictTypeService.checkDictTypeUnique(dict)) {
            return fail("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        return toRes(sysDictTypeService.insertDictType(dict));
    }

    /**
     * 修改字典类型
     */
    @OperateLog(title = "字典类型", operateType = OperateType.UPDATE)
    @PutMapping
    @PreAuthorize("hasAuthority('dict:edit')")
    public Result<?> edit(@Validated @RequestBody SysDictType dict) {
        if (sysDictTypeService.checkDictTypeUnique(dict)) {
            return fail("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        return toRes(sysDictTypeService.updateDictType(dict));
    }

    /**
     * 删除字典类型
     */
    @OperateLog(title = "字典类型", operateType = OperateType.DELETE)
    @DeleteMapping("/{dictIds}")
    @PreAuthorize("hasAuthority('dict:remove')")
    public Result<?> remove(@PathVariable Long[] dictIds) {
        sysDictTypeService.deleteDictTypeByIds(dictIds);
        return success();
    }

    /**
     * 刷新字典缓存
     */
    @OperateLog(title = "字典类型", operateType = OperateType.DELETE)
    @DeleteMapping("/refreshCache")
    @PreAuthorize("hasAuthority('dict:remove')")
    public Result<?> refreshCache() {
        sysDictTypeService.resetDictCache();
        return success();
    }

    /**
     * 获取字典选择框列表
     */
    @GetMapping("/optionselect")
    public Result<?> optionselect() {
        List<SysDictType> sysDictTypes = sysDictTypeMapper.selectList(null);
        return success(sysDictTypes);
    }
}

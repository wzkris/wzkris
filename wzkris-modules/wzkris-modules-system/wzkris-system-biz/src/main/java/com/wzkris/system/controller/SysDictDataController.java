package com.wzkris.system.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.page.Page;
import com.wzkris.system.domain.SysDictData;
import com.wzkris.system.mapper.SysDictDataMapper;
import com.wzkris.system.service.SysDictDataService;
import com.wzkris.system.service.SysDictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
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
public class SysDictDataController extends BaseController {
    private final SysDictDataMapper sysDictDataMapper;
    private final SysDictDataService sysDictDataService;
    private final SysDictTypeService sysDictTypeService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('dict:list')")
    public Result<Page<SysDictData>> list(SysDictData sysDictData) {
        startPage();
        List<SysDictData> list = sysDictDataService.list(sysDictData);
        return getDataTable(list);
    }

    /**
     * 查询字典数据详细
     */
    @GetMapping("/{dictCode}")
    @PreAuthorize("@ps.hasPerms('dict:query')")
    public Result<?> getInfo(@PathVariable Long dictCode) {
        return success(sysDictDataMapper.selectById(dictCode));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping("/type/{dictType}")
    public Result<?> dictType(@PathVariable String dictType) {
        List<SysDictData> data = sysDictTypeService.listDictDataByType(dictType);
        if (StringUtil.isNull(data)) {
            data = new ArrayList<>();
        }
        return success(data);
    }

    /**
     * 新增字典类型
     */
    @OperateLog(title = "字典数据", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('dict:add')")
    public Result<?> add(@Validated @RequestBody SysDictData dict) {
        return toRes(sysDictDataService.insertDictData(dict));
    }

    /**
     * 修改保存字典类型
     */
    @OperateLog(title = "字典数据", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('dict:edit')")
    public Result<?> edit(@Validated @RequestBody SysDictData dict) {
        return toRes(sysDictDataService.updateDictData(dict));
    }

    /**
     * 删除字典类型
     */
    @OperateLog(title = "字典类型", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('dict:remove')")
    public Result<?> remove(@RequestBody Long[] dictCodes) {
        sysDictDataService.deleteDictDataByIds(Arrays.asList(dictCodes));
        return success();
    }
}

package com.thingslink.system.controller;

import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.OperateType;
import com.thingslink.common.orm.page.Page;
import com.thingslink.common.orm.model.BaseController;
import com.thingslink.system.domain.SysDictData;
import com.thingslink.system.mapper.SysDictDataMapper;
import com.thingslink.system.service.SysDictDataService;
import com.thingslink.system.service.SysDictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    @PreAuthorize("hasAuthority('dict:list')")
    public Result<Page<SysDictData>> list(SysDictData sysDictData) {
        startPage();
        List<SysDictData> list = sysDictDataService.list(sysDictData);
        return getDataTable(list);
    }

    /**
     * 查询字典数据详细
     */
    @GetMapping("/{dictCode}")
    @PreAuthorize("hasAuthority('dict:query')")
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
    @PostMapping
    @PreAuthorize("hasAuthority('dict:add')")
    public Result<?> add(@Validated @RequestBody SysDictData dict) {
        return toRes(sysDictDataService.insertDictData(dict));
    }

    /**
     * 修改保存字典类型
     */
    @OperateLog(title = "字典数据", operateType = OperateType.UPDATE)
    @PutMapping
    @PreAuthorize("hasAuthority('dict:edit')")
    public Result<?> edit(@Validated @RequestBody SysDictData dict) {
        return toRes(sysDictDataService.updateDictData(dict));
    }

    /**
     * 删除字典类型
     */
    @OperateLog(title = "字典类型", operateType = OperateType.DELETE)
    @DeleteMapping("/{dictCodes}")
    @PreAuthorize("hasAuthority('dict:remove')")
    public Result<?> remove(@PathVariable Long[] dictCodes) {
        sysDictDataService.deleteDictDataByIds(dictCodes);
        return success();
    }
}

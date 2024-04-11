package com.thingslink.system.controller;

import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.BusinessType;
import com.thingslink.common.orm.page.Page;
import com.thingslink.common.web.controller.BaseController;
import com.thingslink.system.domain.DictData;
import com.thingslink.system.mapper.DictDataMapper;
import com.thingslink.system.service.DictDataService;
import com.thingslink.system.service.DictTypeService;
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
public class DictDataController extends BaseController {
    private final DictDataMapper dictDataMapper;
    private final DictDataService dictDataService;
    private final DictTypeService dictTypeService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('dict:list')")
    public Result<Page<DictData>> list(DictData dictData) {
        startPage();
        List<DictData> list = dictDataService.list(dictData);
        return getDataTable(list);
    }

    /**
     * 查询字典数据详细
     */
    @GetMapping("/{dictCode}")
    @PreAuthorize("hasAuthority('dict:query')")
    public Result<?> getInfo(@PathVariable Long dictCode) {
        return success(dictDataMapper.selectById(dictCode));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping("/type/{dictType}")
    public Result<?> dictType(@PathVariable String dictType) {
        List<DictData> data = dictTypeService.listDictDataByType(dictType);
        if (StringUtil.isNull(data)) {
            data = new ArrayList<>();
        }
        return success(data);
    }

    /**
     * 新增字典类型
     */
    @OperateLog(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    @PreAuthorize("hasAuthority('dict:add')")
    public Result<?> add(@Validated @RequestBody DictData dict) {
        return toRes(dictDataService.insertDictData(dict));
    }

    /**
     * 修改保存字典类型
     */
    @OperateLog(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping
    @PreAuthorize("hasAuthority('dict:edit')")
    public Result<?> edit(@Validated @RequestBody DictData dict) {
        return toRes(dictDataService.updateDictData(dict));
    }

    /**
     * 删除字典类型
     */
    @OperateLog(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}")
    @PreAuthorize("hasAuthority('dict:remove')")
    public Result<?> remove(@PathVariable Long[] dictCodes) {
        dictDataService.deleteDictDataByIds(dictCodes);
        return success();
    }
}

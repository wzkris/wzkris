package com.wzkris.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.security.oauth2.annotation.CheckPerms;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.system.domain.GlobalDictData;
import com.wzkris.system.domain.req.GlobalDictDataQueryReq;
import com.wzkris.system.domain.req.GlobalDictDataReq;
import com.wzkris.system.mapper.GlobalDictDataMapper;
import com.wzkris.system.service.GlobalDictDataService;
import com.wzkris.system.utils.DictCacheUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    @CheckPerms("dict:list")
    public Result<Page<GlobalDictData>> list(GlobalDictDataQueryReq queryReq) {
        startPage();
        LambdaQueryWrapper<GlobalDictData> lqw = this.buildQueryWrapper(queryReq);
        return getDataTable(dictDataMapper.selectList(lqw));
    }

    private LambdaQueryWrapper<GlobalDictData> buildQueryWrapper(GlobalDictDataQueryReq queryReq) {
        return new LambdaQueryWrapper<GlobalDictData>()
                .like(StringUtil.isNotBlank(queryReq.getDictType()), GlobalDictData::getDictType, queryReq.getDictType())
                .like(StringUtil.isNotBlank(queryReq.getDictLabel()), GlobalDictData::getDictLabel, queryReq.getDictLabel())
                .eq(StringUtil.isNotBlank(queryReq.getIsDefault()), GlobalDictData::getIsDefault, queryReq.getIsDefault())
                .orderByDesc(GlobalDictData::getDictSort, GlobalDictData::getDataId);
    }

    @Operation(summary = "详情")
    @GetMapping("/{dictCode}")
    @CheckPerms("dict:query")
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
    @CheckPerms("dict:add")
    public Result<?> add(@Validated @RequestBody GlobalDictDataReq req) {
        dictDataService.insertDictData(BeanUtil.convert(req, GlobalDictData.class));
        return ok();
    }

    @Operation(summary = "修改字典")
    @OperateLog(title = "字典数据", subTitle = "修改字典", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckPerms("dict:edit")
    public Result<?> edit(@Validated @RequestBody GlobalDictDataReq req) {
        dictDataService.updateDictData(BeanUtil.convert(req, GlobalDictData.class));
        return ok();
    }

    @Operation(summary = "删除字典")
    @OperateLog(title = "字典类型", subTitle = "删除字典", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckPerms("dict:remove")
    public Result<?> remove(@RequestBody List<Long> dataIds) {
        dictDataService.deleteDictData(dataIds);
        return ok();
    }
}

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
import com.wzkris.system.domain.GlobalDictType;
import com.wzkris.system.domain.req.GlobalDictTypeQueryReq;
import com.wzkris.system.domain.req.GlobalDictTypeReq;
import com.wzkris.system.mapper.GlobalDictTypeMapper;
import com.wzkris.system.service.GlobalDictTypeService;
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
@Tag(name = "字典类型")
@RestController
@RequestMapping("/dict/type")
@RequiredArgsConstructor
public class GlobalDictTypeController extends BaseController {
    private final GlobalDictTypeMapper dictTypeMapper;
    private final GlobalDictTypeService dictTypeService;


    @Operation(summary = "分页")
    @GetMapping("/list")
    @CheckPerms("dict:list")
    public Result<Page<GlobalDictType>> list(GlobalDictTypeQueryReq queryReq) {
        startPage();
        LambdaQueryWrapper<GlobalDictType> lqw = this.buildQueryWrapper(queryReq);
        return getDataTable(dictTypeMapper.selectList(lqw));
    }

    private LambdaQueryWrapper<GlobalDictType> buildQueryWrapper(GlobalDictTypeQueryReq queryReq) {
        return new LambdaQueryWrapper<GlobalDictType>()
                .like(StringUtil.isNotBlank(queryReq.getDictName()), GlobalDictType::getDictName, queryReq.getDictName())
                .like(StringUtil.isNotBlank(queryReq.getDictType()), GlobalDictType::getDictType, queryReq.getDictType())
                .orderByDesc(GlobalDictType::getTypeId);
    }

    @Operation(summary = "详情")
    @GetMapping("/{dictId}")
    @CheckPerms("dict:query")
    public Result<GlobalDictType> getInfo(@PathVariable Long dictId) {
        return ok(dictTypeMapper.selectById(dictId));
    }

    @Operation(summary = "新增")
    @OperateLog(title = "字典类型", subTitle = "添加字典", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckPerms("dict:add")
    public Result<Void> add(@Validated @RequestBody GlobalDictTypeReq req) {
        if (dictTypeService.checkDictTypeUnique(req.getTypeId(), req.getDictType())) {
            return fail("新增字典'" + req.getDictName() + "'失败，字典类型已存在");
        }
        return toRes(dictTypeService.insertDictType(BeanUtil.convert(req, GlobalDictType.class)));
    }

    @Operation(summary = "修改")
    @OperateLog(title = "字典类型", subTitle = "修改字典", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckPerms("dict:edit")
    public Result<Void> edit(@Validated @RequestBody GlobalDictTypeReq req) {
        if (dictTypeService.checkDictTypeUnique(req.getTypeId(), req.getDictType())) {
            return fail("修改字典'" + req.getDictName() + "'失败，字典类型已存在");
        }
        return toRes(dictTypeService.updateDictType(BeanUtil.convert(req, GlobalDictType.class)));
    }

    @Operation(summary = "删除")
    @OperateLog(title = "字典类型", subTitle = "删除字典", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckPerms("dict:remove")
    public Result<Void> remove(@RequestBody List<Long> typeIds) {
        if (dictTypeService.checkDictTypeUsed(typeIds)) {
            return fail("删除失败，该字典类型已被使用");
        }
        dictTypeService.deleteByIds(typeIds);
        return ok();
    }

    @Operation(summary = "刷新字典缓存")
    @PostMapping("/refreshCache")
    @CheckPerms("dict:remove")
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

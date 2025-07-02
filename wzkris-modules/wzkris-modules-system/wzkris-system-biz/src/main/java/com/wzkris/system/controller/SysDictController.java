package com.wzkris.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.oauth2.annotation.CheckSystemPerms;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.system.domain.SysDict;
import com.wzkris.system.domain.req.SysDictQueryReq;
import com.wzkris.system.domain.req.SysDictReq;
import com.wzkris.system.mapper.SysDictMapper;
import com.wzkris.system.service.SysDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 数据字典信息
 *
 * @author wzkris
 */
@Tag(name = "字典管理")
@RestController
@RequestMapping("/sys_dict")
@RequiredArgsConstructor
public class SysDictController extends BaseController {

    private final SysDictMapper dictMapper;

    private final SysDictService dictService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @CheckSystemPerms("sys_dict:list")
    public Result<Page<SysDict>> list(SysDictQueryReq queryReq) {
        startPage();
        LambdaQueryWrapper<SysDict> lqw = this.buildQueryWrapper(queryReq);
        return getDataTable(dictMapper.selectList(lqw));
    }

    private LambdaQueryWrapper<SysDict> buildQueryWrapper(SysDictQueryReq queryReq) {
        return new LambdaQueryWrapper<SysDict>()
                .like(StringUtil.isNotBlank(queryReq.getDictName()), SysDict::getDictName, queryReq.getDictName())
                .like(StringUtil.isNotBlank(queryReq.getDictKey()), SysDict::getDictKey, queryReq.getDictKey())
                .orderByDesc(SysDict::getDictId);
    }

    @Operation(summary = "详情")
    @GetMapping("/{dictId}")
    @CheckSystemPerms("sys_dict:query")
    public Result<SysDict> getInfo(@PathVariable Long dictId) {
        return ok(dictMapper.selectById(dictId));
    }

    @Operation(summary = "新增")
    @OperateLog(title = "数据字典", subTitle = "添加字典", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckSystemPerms("sys_dict:add")
    public Result<Void> add(@Validated @RequestBody SysDictReq req) {
        if (dictService.checkUsedByDictKey(req.getDictId(), req.getDictKey())) {
            return err412("新增字典'" + req.getDictName() + "'失败，字典类型已存在");
        }
        return toRes(dictService.insertDict(BeanUtil.convert(req, SysDict.class)));
    }

    @Operation(summary = "修改")
    @OperateLog(title = "数据字典", subTitle = "修改字典", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckSystemPerms("sys_dict:edit")
    public Result<Void> edit(@Validated @RequestBody SysDictReq req) {
        if (dictService.checkUsedByDictKey(req.getDictId(), req.getDictKey())) {
            return err412("修改字典'" + req.getDictName() + "'失败，字典类型已存在");
        }
        return toRes(dictService.updateDict(BeanUtil.convert(req, SysDict.class)));
    }

    @Operation(summary = "删除")
    @OperateLog(title = "数据字典", subTitle = "删除字典", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckSystemPerms("sys_dict:remove")
    public Result<Void> remove(@RequestBody Long dictId) {
        return toRes(dictService.deleteById(dictId));
    }

    @Operation(summary = "刷新字典缓存")
    @PostMapping("/refreshCache")
    @CheckSystemPerms("sys_dict:remove")
    public Result<?> refreshCache() {
        dictService.loadingDictCache();
        return ok();
    }

}

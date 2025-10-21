package com.wzkris.message.controller.dictionary;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckUserPerms;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.message.domain.DictionaryInfoDO;
import com.wzkris.message.domain.req.dictionary.DictionaryManageQueryReq;
import com.wzkris.message.domain.req.dictionary.DictionaryManageReq;
import com.wzkris.message.mapper.DictionaryInfoMapper;
import com.wzkris.message.service.DictionaryInfoService;
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
@Validated
@RestController
@RequestMapping("/dictionary-manage")
@RequiredArgsConstructor
public class DictionaryInfoManageController extends BaseController {

    private final DictionaryInfoMapper dictionaryInfoMapper;

    private final DictionaryInfoService dictionaryInfoService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @CheckUserPerms("msg-mod:dictionary-mng:list")
    public Result<Page<DictionaryInfoDO>> list(DictionaryManageQueryReq queryReq) {
        startPage();
        LambdaQueryWrapper<DictionaryInfoDO> lqw = this.buildQueryWrapper(queryReq);
        return getDataTable(dictionaryInfoMapper.selectList(lqw));
    }

    private LambdaQueryWrapper<DictionaryInfoDO> buildQueryWrapper(DictionaryManageQueryReq queryReq) {
        return new LambdaQueryWrapper<DictionaryInfoDO>()
                .like(StringUtil.isNotBlank(queryReq.getDictName()), DictionaryInfoDO::getDictName, queryReq.getDictName())
                .like(StringUtil.isNotBlank(queryReq.getDictKey()), DictionaryInfoDO::getDictKey, queryReq.getDictKey())
                .orderByDesc(DictionaryInfoDO::getDictId);
    }

    @Operation(summary = "详情")
    @GetMapping("/{dictId}")
    @CheckUserPerms("msg-mod:dictionary-mng:list")
    public Result<DictionaryInfoDO> getInfo(@PathVariable Long dictId) {
        return ok(dictionaryInfoMapper.selectById(dictId));
    }

    @Operation(summary = "新增")
    @OperateLog(title = "数据字典", subTitle = "添加字典", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckUserPerms("msg-mod:dictionary-mng:add")
    public Result<Void> add(@RequestBody DictionaryManageReq req) {
        if (dictionaryInfoService.checkUsedByDictKey(req.getDictId(), req.getDictKey())) {
            return err40000("新增字典'" + req.getDictName() + "'失败，字典类型已存在");
        }
        return toRes(dictionaryInfoService.insertDict(BeanUtil.convert(req, DictionaryInfoDO.class)));
    }

    @Operation(summary = "修改")
    @OperateLog(title = "数据字典", subTitle = "修改字典", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckUserPerms("msg-mod:dictionary-mng:edit")
    public Result<Void> edit(@RequestBody DictionaryManageReq req) {
        if (dictionaryInfoService.checkUsedByDictKey(req.getDictId(), req.getDictKey())) {
            return err40000("修改字典'" + req.getDictName() + "'失败，字典类型已存在");
        }
        return toRes(dictionaryInfoService.updateDict(BeanUtil.convert(req, DictionaryInfoDO.class)));
    }

    @Operation(summary = "删除")
    @OperateLog(title = "数据字典", subTitle = "删除字典", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckUserPerms("msg-mod:dictionary-mng:remove")
    public Result<Void> remove(@RequestBody Long dictId) {
        return toRes(dictionaryInfoService.deleteById(dictId));
    }

    @Operation(summary = "刷新字典缓存")
    @PostMapping("/refresh-cache")
    @CheckUserPerms("msg-mod:dictionary-mng:remove")
    public Result<?> refreshCache() {
        dictionaryInfoService.loadingDictCache();
        return ok();
    }

}

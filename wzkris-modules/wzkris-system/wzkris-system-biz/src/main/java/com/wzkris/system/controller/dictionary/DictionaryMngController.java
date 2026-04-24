package com.wzkris.system.controller.dictionary;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.system.api.dictionary.DictionaryMngApi;
import com.wzkris.system.request.common.IdRequest;
import com.wzkris.system.request.dictionary.DictionaryMngPageRequest;
import com.wzkris.system.request.dictionary.DictionaryMngSaveRequest;
import com.wzkris.system.request.dictionary.DictionaryMngUpdateRequest;
import com.wzkris.system.response.dictionary.DictionaryInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
public class DictionaryMngController {

    private final DictionaryMngApi dictionaryMngApi;

    @Operation(summary = "分页")
    @GetMapping("/query-page")
    @CheckAdminPerms("system-mod:dictionary-mng:page")
    public Result<Page<DictionaryInfoResponse>> queryPage(DictionaryMngPageRequest request) {
        return dictionaryMngApi.queryPage(request);
    }

    @Operation(summary = "详情")
    @GetMapping("/query-info/{id}")
    @CheckAdminPerms("system-mod:dictionary-mng:page")
    public Result<DictionaryInfoResponse> queryInfo(IdRequest request) {
        return dictionaryMngApi.queryInfo(request);
    }

    @Operation(summary = "新增")
    @OperateLog(title = "数据字典", subTitle = "添加字典", type = OperateTypeEnum.INSERT)
    @PostMapping("/save")
    @CheckAdminPerms("system-mod:dictionary-mng:add")
    public Result<Void> save(@RequestBody DictionaryMngSaveRequest addReq) {
        return dictionaryMngApi.save(addReq);
    }

    @Operation(summary = "修改")
    @OperateLog(title = "数据字典", subTitle = "修改字典", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update")
    @CheckAdminPerms("system-mod:dictionary-mng:edit")
    public Result<Void> update(@RequestBody DictionaryMngUpdateRequest request) {
        return dictionaryMngApi.update(request);
    }

    @Operation(summary = "删除")
    @OperateLog(title = "数据字典", subTitle = "删除字典", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckAdminPerms("system-mod:dictionary-mng:remove")
    public Result<Void> remove(@RequestBody @Valid IdRequest request) {
        return dictionaryMngApi.remove(request);
    }

    @Operation(summary = "刷新字典缓存")
    @PostMapping("/refresh-cache")
    @CheckAdminPerms("system-mod:dictionary-mng:remove")
    public Result<?> refreshCache() {
        return dictionaryMngApi.refreshCache();
    }

}


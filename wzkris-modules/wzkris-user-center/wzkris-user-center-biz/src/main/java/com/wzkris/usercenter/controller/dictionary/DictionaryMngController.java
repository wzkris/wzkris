package com.wzkris.usercenter.controller.dictionary;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.usercenter.api.dictionary.DictionaryMngApi;
import com.wzkris.usercenter.api.dictionary.request.DictionaryMngPageRequest;
import com.wzkris.usercenter.api.dictionary.request.DictionaryMngSaveRequest;
import com.wzkris.usercenter.api.dictionary.request.DictionaryMngUpdateRequest;
import com.wzkris.usercenter.api.dictionary.response.DictionaryMngResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "字典管理")
@Validated
@RestController
@RequestMapping("/dictionary-manage")
@RequiredArgsConstructor
public class DictionaryMngController {

    private final DictionaryMngApi dictionaryMngApi;

    @Operation(summary = "分页")
    @GetMapping("/query-page")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, value = "system-mod:dictionary-mng:page")
    public Result<Page<DictionaryMngResponse>> queryPage(@ParameterObject DictionaryMngPageRequest request) {
        return dictionaryMngApi.queryPage(request);
    }

    @Operation(summary = "详情")
    @GetMapping("/query-info/{id}")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, value = "system-mod:dictionary-mng:page")
    public Result<DictionaryMngResponse> queryInfo(@ParameterObject IdRequest request) {
        return dictionaryMngApi.queryInfo(request);
    }

    @Operation(summary = "新增")
    @OperateLog(title = "数据字典", subTitle = "添加字典", type = OperateTypeEnum.INSERT)
    @PostMapping("/save")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, value = "system-mod:dictionary-mng:add")
    public Result<Void> save(@RequestBody DictionaryMngSaveRequest request) {
        return dictionaryMngApi.save(request);
    }

    @Operation(summary = "修改")
    @OperateLog(title = "数据字典", subTitle = "修改字典", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, value = "system-mod:dictionary-mng:edit")
    public Result<Void> update(@RequestBody DictionaryMngUpdateRequest request) {
        return dictionaryMngApi.update(request);
    }

    @Operation(summary = "删除")
    @OperateLog(title = "数据字典", subTitle = "删除字典", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, value = "system-mod:dictionary-mng:remove")
    public Result<Void> remove(@RequestBody @Valid IdRequest request) {
        return dictionaryMngApi.remove(request);
    }

    @Operation(summary = "刷新字典缓存")
    @PostMapping("/refresh-cache")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, value = "system-mod:dictionary-mng:remove")
    public Result<?> refreshCache() {
        return dictionaryMngApi.refreshCache();
    }

}


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
import com.wzkris.usercenter.api.dictionary.response.DictionaryMngQueryResponse;
import com.wzkris.usercenter.api.dictionary.response.DictionaryMngPageResponse;
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

    private static final String PERM_PREFIX = "system-mod:dictionary-mng:";

    private final DictionaryMngApi dictionaryMngApi;

    @Operation(summary = "分页")
    @GetMapping("/query-page")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "page")
    public Result<Page<DictionaryMngPageResponse>> queryPage(@ParameterObject DictionaryMngPageRequest request) {
        return dictionaryMngApi.queryPage(request);
    }

    @Operation(summary = "详情")
    @GetMapping("/query-id/{id}")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "page")
    public Result<DictionaryMngQueryResponse> queryById(@ParameterObject IdRequest request) {
        return dictionaryMngApi.queryById(request);
    }

    @Operation(summary = "新增")
    @OperateLog(title = "数据字典", subTitle = "添加字典", type = OperateTypeEnum.INSERT)
    @PostMapping("/save")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "add")
    public Result<Void> save(@RequestBody DictionaryMngSaveRequest request) {
        return dictionaryMngApi.save(request);
    }

    @Operation(summary = "修改")
    @OperateLog(title = "数据字典", subTitle = "修改字典", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "edit")
    public Result<Void> update(@RequestBody DictionaryMngUpdateRequest request) {
        return dictionaryMngApi.update(request);
    }

    @Operation(summary = "删除")
    @OperateLog(title = "数据字典", subTitle = "删除字典", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "remove")
    public Result<Void> remove(@RequestBody @Valid IdRequest request) {
        return dictionaryMngApi.remove(request);
    }

    @Operation(summary = "刷新字典缓存")
    @PostMapping("/refresh-cache")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, prefix = PERM_PREFIX, value = "remove")
    public Result<?> refreshCache() {
        return dictionaryMngApi.refreshCache();
    }

}


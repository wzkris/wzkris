package com.wzkris.system.controller.dictionary;

import com.wzkris.common.core.model.Result;
import com.wzkris.system.api.dictionary.DictionaryInfoApi;
import com.wzkris.system.api.dictionary.request.DictionaryInfoListRequest;
import com.wzkris.system.api.dictionary.response.DictionaryDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "字典信息")
@RestController
@RequestMapping("/dictionary-info")
@RequiredArgsConstructor
public class DictionaryInfoController {

    private final DictionaryInfoApi dictionaryInfoApi;

    @Operation(summary = "查询字典")
    @GetMapping("/{dictKey}")
    public Result<List<DictionaryDataResponse>> queryValue(@ParameterObject DictionaryInfoListRequest request) {
        return dictionaryInfoApi.queryValue(request);
    }

}

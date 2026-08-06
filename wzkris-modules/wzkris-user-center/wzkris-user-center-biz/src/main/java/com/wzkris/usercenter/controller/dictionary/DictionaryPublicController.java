package com.wzkris.usercenter.controller.dictionary;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.api.dictionary.DictionaryPublicApi;
import com.wzkris.usercenter.api.dictionary.request.DictionaryPublicListRequest;
import com.wzkris.usercenter.api.dictionary.response.DictionaryDataResponse;
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
@RequestMapping("/dictionary-public")
@RequiredArgsConstructor
public class DictionaryPublicController {

    private final DictionaryPublicApi dictionaryPublicApi;

    @Operation(summary = "查询字典")
    @GetMapping("/{dictKey}")
    public Result<List<DictionaryDataResponse>> queryValue(@ParameterObject DictionaryPublicListRequest request) {
        return dictionaryPublicApi.queryValue(request);
    }

}

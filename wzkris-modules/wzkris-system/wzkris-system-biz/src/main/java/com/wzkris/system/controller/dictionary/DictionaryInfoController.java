package com.wzkris.system.controller.dictionary;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.system.domain.DictionaryInfoDO;
import com.wzkris.system.service.DictionaryInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "字典信息")
@RestController
@RequestMapping("/dictionary-info")
@RequiredArgsConstructor
public class DictionaryInfoController extends BaseController {

    private final DictionaryInfoService dictService;

    @Operation(summary = "查询字典")
    @GetMapping("/{dictKey}")
    public Result<DictionaryInfoDO.DictData[]> queryValue(@PathVariable String dictKey) {
        return ok(dictService.getValueByKey(dictKey));
    }

}

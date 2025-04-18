package com.wzkris.system.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.system.domain.SysDict;
import com.wzkris.system.service.SysDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "字典查询")
@RestController
@RequestMapping("/dictionary")
@RequiredArgsConstructor
public class SysDictOwnController extends BaseController {

    private final SysDictService dictService;

    @Operation(summary = "查询字典")
    @GetMapping("/{dictKey}")
    public Result<SysDict.DictData[]> list(@PathVariable String dictKey) {
        return ok(dictService.getValueByDictKey(dictKey));
    }
}

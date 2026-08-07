package com.wzkris.usercenter.controller.customer;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.api.customer.CustomerInfoApi;
import com.wzkris.usercenter.api.customer.request.CustomerInfoBasicUpdateRequest;
import com.wzkris.usercenter.api.customer.response.CustomerInfoQueryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "客户信息")
@Slf4j
@Validated
@RestController
@RequestMapping("/customer-info")
@RequiredArgsConstructor
public class CustomerInfoController {

    private final String info_prefix = "customer-info";

    private final CustomerInfoApi customerInfoApi;

    @Operation(summary = "获取信息")
    @GetMapping("/query")
    @Cacheable(value = info_prefix + "#3_600_000", key = "@uch.getLoginUser().getUid()", sync = true)
    public Result<CustomerInfoQueryResponse> query() {
        return customerInfoApi.query();
    }

    @Operation(summary = "修改信息")
    @PostMapping("/update-basic")
    @CacheEvict(value = info_prefix, key = "@uch.getLoginUser().getUid()")
    public Result<?> updateBasicInfo(@RequestBody CustomerInfoBasicUpdateRequest request) {
        return customerInfoApi.updateBasicInfo(request);
    }

}


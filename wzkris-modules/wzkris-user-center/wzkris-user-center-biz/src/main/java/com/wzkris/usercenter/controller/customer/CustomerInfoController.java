package com.wzkris.usercenter.controller.customer;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.api.customer.CustomerInfoApi;
import com.wzkris.usercenter.request.customer.CustomerInfoBasicUpdateRequest;
import com.wzkris.usercenter.response.customer.CustomerInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户个人信息
 *
 * @author wzkris
 */
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
    @GetMapping("/query-info")
    @Cacheable(value = info_prefix + "#3_600_000", key = "@su.getUid()", sync = true)
    public Result<CustomerInfoResponse> queryInfo() {
        return customerInfoApi.queryInfo();
    }

    @Operation(summary = "修改信息")
    @PostMapping("/update-basic")
    @CacheEvict(value = info_prefix, key = "@su.getUid()")
    public Result<?> updateBasicInfo(@RequestBody CustomerInfoBasicUpdateRequest request) {
        return customerInfoApi.updateBasicInfo(request);
    }

}


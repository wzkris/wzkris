package com.wzkris.usercenter.controller.customerlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.customerlog.operate.CustomerOperateLogInfoApi;
import com.wzkris.usercenter.api.customerlog.operate.request.CustomerOperateLogInfoPageRequest;
import com.wzkris.usercenter.api.customerlog.operate.response.CustomerOperateLogInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户个人操作日志信息")
@RestController
@RequestMapping("/customer-operatelog-info")
@RequiredArgsConstructor
public class CustomerOperateLogInfoController {

    private final CustomerOperateLogInfoApi customerOperateLogInfoApi;

    @Operation(summary = "分页")
    @GetMapping("/query-page")
    public Result<Page<CustomerOperateLogInfoResponse>> queryPage(@ParameterObject CustomerOperateLogInfoPageRequest request) {
        return customerOperateLogInfoApi.queryPage(request);
    }

}

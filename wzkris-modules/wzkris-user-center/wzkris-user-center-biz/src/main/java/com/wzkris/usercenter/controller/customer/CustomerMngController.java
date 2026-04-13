package com.wzkris.usercenter.controller.customer;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.usercenter.api.customer.CustomerMngApi;
import com.wzkris.usercenter.request.StatusUpdateRequest;
import com.wzkris.usercenter.request.customer.CustomerMngQueryRequest;
import com.wzkris.usercenter.response.customer.CustomerMngResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "客户管理")
@Validated
@RestController
@RequestMapping("/customer-manage")
@RequiredArgsConstructor
public class CustomerMngController {

    private final CustomerMngApi customerMngApi;

    @Operation(summary = "客户分页列表")
    @GetMapping("/query-page")
    @CheckAdminPerms("user-mod:customer-mng:page")
    public Result<Page<CustomerMngResponse>> queryPage(CustomerMngQueryRequest request) {
        return customerMngApi.queryPage(request);
    }

    @Operation(summary = "客户详细信息")
    @GetMapping("/query-info/{customerId}")
    @CheckAdminPerms("user-mod:customer-mng:query")
    public Result<CustomerMngResponse> queryInfo(@PathVariable Long customerId) {
        return customerMngApi.queryInfo(customerId);
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "客户管理", subTitle = "状态修改", type = OperateTypeEnum.UPDATE)
    @PostMapping("/update-status")
    @CheckAdminPerms("user-mod:customer-mng:edit")
    public Result<Void> updateStatus(@RequestBody StatusUpdateRequest request) {
        return customerMngApi.updateStatus(request);
    }

    @Operation(summary = "导出")
    @OperateLog(title = "客户管理", type = OperateTypeEnum.EXPORT)
    @GetMapping("/export")
    @CheckAdminPerms("user-mod:customer-mng:export")
    public void export(HttpServletResponse response, CustomerMngQueryRequest request) {
        customerMngApi.export(response, request);
    }

}


package com.wzkris.usercenter.controller.customer;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.usercenter.api.customer.CustomerMngApi;
import com.wzkris.usercenter.request.common.IdRequest;
import com.wzkris.usercenter.request.customer.CustomerMngPageRequest;
import com.wzkris.usercenter.response.customer.CustomerMngResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Result<Page<CustomerMngResponse>> queryPage(CustomerMngPageRequest request) {
        return customerMngApi.queryPage(request);
    }

    @Operation(summary = "客户详细信息")
    @GetMapping("/query-info/{id}")
    @CheckAdminPerms("user-mod:customer-mng:query")
    public Result<CustomerMngResponse> queryInfo(IdRequest request) {
        return customerMngApi.queryInfo(request);
    }

    @Operation(summary = "导出")
    @OperateLog(title = "客户管理", type = OperateTypeEnum.EXPORT)
    @GetMapping("/export")
    @CheckAdminPerms("user-mod:customer-mng:export")
    public void export(HttpServletResponse response, CustomerMngPageRequest request) {
        customerMngApi.export(response, request);
    }

}


package com.wzkris.usercenter.controller.customerlog.login;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.customerlog.login.CustomerLoginlogInfoApi;
import com.wzkris.usercenter.api.customerlog.login.request.CustomerLoginLogInfoPageRequest;
import com.wzkris.usercenter.api.customerlog.login.response.CustomerLoginLogInfoPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户个人登录日志信息")
@RestController
@RequestMapping("/customer-loginlog-info")
@RequiredArgsConstructor
public class CustomerLoginlogInfoController {

    private final CustomerLoginlogInfoApi customerLoginlogInfoApi;

    @Operation(summary = "分页")
    @GetMapping("/query-page")
    public Result<Page<CustomerLoginLogInfoPageResponse>> queryPage(@ParameterObject CustomerLoginLogInfoPageRequest request) {
        return customerLoginlogInfoApi.queryPage(request);
    }

}

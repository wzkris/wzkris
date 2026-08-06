package com.wzkris.usercenter.remote.controller.customer;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.customer.CustomerRemoteApi;
import com.wzkris.usercenter.remote.api.customer.request.CustomerQueryRequest;
import com.wzkris.usercenter.remote.api.customer.request.WexcxLoginRequest;
import com.wzkris.usercenter.remote.api.customer.response.CustomerQueryResponse;
import com.wzkris.usercenter.remote.api.customer.response.CustomerListResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/customer-remote")
@RequiredArgsConstructor
public class CustomerRemoteController {

    private final CustomerRemoteApi customerRemoteApi;

    @PostMapping("/query-list")
    public Result<List<CustomerListResponse>> queryList(@RequestBody @Valid CustomerQueryRequest request) {
        return customerRemoteApi.queryList(request);
    }

    @PostMapping("/wexcx-login")
    public Result<CustomerQueryResponse> wexcxLogin(@RequestBody WexcxLoginRequest request) {
        return customerRemoteApi.wexcxLogin(request);
    }

    @PostMapping("/update-logininfo")
    public Result<Void> updateLoginInfo(@RequestBody LoginInfoUpdateRequest request) {
        return customerRemoteApi.updateLoginInfo(request);
    }

}

package com.wzkris.usercenter.remote.controller.customer;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.customer.CustomerInfoRemoteApi;
import com.wzkris.usercenter.remote.api.customer.request.WexcxLoginRequest;
import com.wzkris.usercenter.remote.api.customer.response.CustomerResponse;
import com.wzkris.usercenter.request.common.StringValueRequest;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/customer-info-remote")
@RequiredArgsConstructor
public class CustomerInfoRemoteController {

    private final CustomerInfoRemoteApi customerInfoRemoteApi;

    @PostMapping("/query-by-phonenumber")
    public Result<CustomerResponse> queryByPhoneNumber(@RequestBody @Valid StringValueRequest request) {
        return customerInfoRemoteApi.queryByPhoneNumber(request);
    }

    @PostMapping("/wexcx-login")
    public Result<CustomerResponse> wexcxLogin(@RequestBody WexcxLoginRequest request) {
        return customerInfoRemoteApi.wexcxLogin(request);
    }

    @PostMapping("/update-logininfo")
    public Result<Void> updateLoginInfo(@RequestBody LoginInfoUpdateRequest request) {
        return customerInfoRemoteApi.updateLoginInfo(request);
    }

}


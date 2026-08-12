package com.wzkris.usercenter.remote.controller.customer;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.admin.request.LoginInfoUpdateRequest;
import com.wzkris.usercenter.remote.api.customer.CustomerRemoteApi;
import com.wzkris.usercenter.remote.api.customer.request.CustomerQueryRequest;
import com.wzkris.usercenter.remote.api.customer.request.CustomerSocialUpdateRequest;
import com.wzkris.usercenter.remote.api.customer.request.SocialLoginRequest;
import com.wzkris.usercenter.remote.api.customer.response.CustomerQueryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "客户")
@RestController
@RequestMapping("/customer-remote")
@RequiredArgsConstructor
public class CustomerRemoteController {

    private final CustomerRemoteApi customerRemoteApi;

    @Operation(summary = "查询客户列表")
    @PostMapping("/query-list")
    public Result<List<CustomerQueryResponse>> queryList(@RequestBody @Valid CustomerQueryRequest request) {
        return customerRemoteApi.queryList(request);
    }

    @Operation(summary = "根据社交code查询客户")
    @PostMapping("/query-by-social")
    public Result<CustomerQueryResponse> socialLogin(@RequestBody SocialLoginRequest request) {
        return customerRemoteApi.socialLogin(request);
    }

    @Operation(summary = "更新客户社交账号绑定")
    @PostMapping("/update-social-info")
    public Result<Void> updateSocialInfo(@RequestBody @Valid CustomerSocialUpdateRequest request) {
        return customerRemoteApi.updateSocialInfo(request);
    }

    @Operation(summary = "更新客户登录信息")
    @PostMapping("/update-logininfo")
    public Result<Void> updateLoginInfo(@RequestBody LoginInfoUpdateRequest request) {
        return customerRemoteApi.updateLoginInfo(request);
    }

}

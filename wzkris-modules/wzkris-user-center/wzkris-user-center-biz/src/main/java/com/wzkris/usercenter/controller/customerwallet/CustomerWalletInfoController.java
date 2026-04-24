package com.wzkris.usercenter.controller.customerwallet;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.customerwallet.CustomerWalletInfoApi;
import com.wzkris.usercenter.request.customerwallet.CustomerWalletRecordPageRequest;
import com.wzkris.usercenter.response.customerwallet.CustomerWalletInfoResponse;
import com.wzkris.usercenter.response.customerwallet.CustomerWalletRecordResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "客户钱包信息")
@Validated
@RestController
@RequestMapping("/customer-wallet-info")
@RequiredArgsConstructor
public class CustomerWalletInfoController {

    private final CustomerWalletInfoApi customerWalletInfoApi;

    @Operation(summary = "余额信息")
    @GetMapping("/query-info")
    public Result<CustomerWalletInfoResponse> queryInfo() {
        return customerWalletInfoApi.queryInfo();
    }

    @Operation(summary = "钱包记录")
    @GetMapping("/query-record-page")
    public Result<Page<CustomerWalletRecordResponse>> queryRecordPage(CustomerWalletRecordPageRequest request) {
        return customerWalletInfoApi.queryRecordPage(request);
    }

}


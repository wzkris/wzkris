package com.wzkris.auth.controller;

import com.wzkris.auth.api.WxJsTicketApi;
import com.wzkris.common.core.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信js签名
 *
 * @author wzkris
 */
@Slf4j
@Tag(name = "微信请求API")
@Validated
@RestController
@RequestMapping("/wx_req")
@RequiredArgsConstructor
public class WxJsTicketController {

    private final WxJsTicketApi wxJsTicketApi;

    @Operation(summary = "获取jsticket")
    @GetMapping("/query-js-ticket")
    public Result<?> jsticket() {
        return wxJsTicketApi.queryJsticket();
    }

    @Operation(summary = "获取jsticket签名")
    @GetMapping("/query-js-ticket-sign")
    public Result<?> JsapiSignature(String url) {
        return wxJsTicketApi.queryJsapiSignature(url);
    }

}

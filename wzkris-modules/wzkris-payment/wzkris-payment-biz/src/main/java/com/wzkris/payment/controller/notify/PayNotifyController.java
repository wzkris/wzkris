package com.wzkris.payment.controller.notify;

import com.wzkris.payment.api.notify.PayNotifyApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 渠道【支付】异步回调入口（免鉴权：不标 @CheckPerms，对齐 auth 服务公开端点模式）
 *
 * <p>路径带 configId：多商户场景下按配置精确路由，验签/解密使用对应商户配置，渠道由配置派生。
 * 退款回调见 {@link RefundNotifyController}。
 *
 * @author wzkris
 */
@Tag(name = "支付回调")
@RestController
@RequestMapping("/pay/notify")
@RequiredArgsConstructor
public class PayNotifyController {

    private final PayNotifyApi notifyApi;

    @Operation(summary = "渠道支付异步回调")
    @PostMapping("/{configId}")
    public String payNotify(@PathVariable Long configId,
                            @RequestBody String body,
                            @RequestHeader Map<String, String> headers) {
        return notifyApi.handle(configId, body, headers);
    }

}

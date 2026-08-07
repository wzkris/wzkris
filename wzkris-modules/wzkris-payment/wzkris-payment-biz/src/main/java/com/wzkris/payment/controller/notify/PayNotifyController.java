package com.wzkris.payment.controller.notify;

import com.wzkris.payment.api.notify.PayNotifyApi;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 渠道异步回调入口（免鉴权：不标 @CheckPerms，对齐 auth 服务公开端点模式）
 *
 * <p>路径带 configId：多商户场景下按配置精确路由，验签/解密使用对应商户配置。
 *
 * @author wzkris
 */
@Tag(name = "支付回调")
@RestController
@RequestMapping("/pay/notify")
@RequiredArgsConstructor
public class PayNotifyController {

    private final PayNotifyApi notifyApi;

    @Operation(summary = "渠道异步回调")
    @PostMapping("/{channel}/{configId}")
    public String notify(@PathVariable String channel,
                         @PathVariable Long configId,
                         @RequestBody String body,
                         @RequestHeader Map<String, String> headers) {
        // 无效渠道路径无对应 provider，无法由渠道决定 ACK，回通用 NACK 促使渠道重试
        PayChannelEnum ch = PayChannelEnum.fromValue(channel);
        if (ch == null) {
            return "fail";
        }
        return notifyApi.handleNotify(ch, configId, body, headers);
    }

}

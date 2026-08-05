package com.wzkris.payment.controller.notify;

import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.service.PayNotifyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private final PayNotifyService notifyService;

    @Operation(summary = "渠道异步回调")
    @PostMapping("/{channel}/{configId}")
    public String notify(@PathVariable String channel,
                         @PathVariable Long configId,
                         @RequestBody String body,
                         @RequestHeader Map<String, String> headers) {
        PayChannelEnum ch = PayChannelEnum.fromValue(channel.toUpperCase());
        if (ch == null) {
            return "fail";
        }
        return notifyService.handleNotify(ch, configId, body, headers);
    }
}

package com.wzkris.payment.impl.notify;

import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.payment.api.notify.PayNotifyApi;
import com.wzkris.payment.enums.channel.PayChannelEnum;
import com.wzkris.payment.service.PayNotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 渠道回调 Api 实现：薄编排层，落库/验签/状态机交由 {@link PayNotifyService}。
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class PayNotifyApiImpl extends AbstractApi implements PayNotifyApi {

    private final PayNotifyService notifyService;

    @Override
    public String handleNotify(PayChannelEnum channel, Long configId, String body, Map<String, String> headers) {
        return notifyService.handleNotify(channel, configId, body, headers);
    }

}

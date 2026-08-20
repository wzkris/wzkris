// com.wzkris.payment.impl.notify.PayNotifyApiImpl.java
package com.wzkris.payment.impl.notify;

import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.payment.api.notify.PayNotifyApi;
import com.wzkris.payment.service.PayNotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 渠道【支付】异步回调入口——按约定仅做 controller 透传，编排在 {@link PayNotifyService}。
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class PayNotifyApiImpl extends AbstractApi implements PayNotifyApi {

    private final PayNotifyService payNotifyService;

    @Override
    public String handle(Long configId, String body, Map<String, String> headers) {
        return payNotifyService.handle(configId, body, headers);
    }

}

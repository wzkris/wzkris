// com.wzkris.payment.impl.notify.RefundNotifyApiImpl.java
package com.wzkris.payment.impl.notify;

import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.payment.api.notify.RefundNotifyApi;
import com.wzkris.payment.service.RefundNotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 渠道【退款】异步回调入口——按约定仅做 controller 透传，编排在 {@link RefundNotifyService}。
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class RefundNotifyApiImpl extends AbstractApi implements RefundNotifyApi {

    private final RefundNotifyService refundNotifyService;

    @Override
    public String handle(Long configId, String body, Map<String, String> headers) {
        return refundNotifyService.handle(configId, body, headers);
    }

}

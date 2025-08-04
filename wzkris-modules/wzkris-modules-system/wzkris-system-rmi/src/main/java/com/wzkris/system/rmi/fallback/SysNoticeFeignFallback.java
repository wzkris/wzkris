package com.wzkris.system.rmi.fallback;

import com.wzkris.common.openfeign.core.FeignLogAggregator;
import com.wzkris.system.rmi.SysNoticeFeign;
import com.wzkris.system.rmi.domain.req.SendNoticeReq;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class SysNoticeFeignFallback implements FallbackFactory<SysNoticeFeign> {

    @Override
    public SysNoticeFeign create(Throwable cause) {
        return new SysNoticeFeign() {
            @Override
            public void send2Users(SendNoticeReq sendNoticeReq) {
                FeignLogAggregator.INSTANCE.logPrintError(this.getClass(), cause);
            }
        };
    }

}

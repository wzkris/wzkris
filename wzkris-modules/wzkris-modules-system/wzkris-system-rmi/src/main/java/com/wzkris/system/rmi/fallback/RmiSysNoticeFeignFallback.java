package com.wzkris.system.rmi.fallback;

import com.wzkris.system.rmi.RmiSysNoticeFeign;
import com.wzkris.system.rmi.domain.req.SendNoticeReq;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class RmiSysNoticeFeignFallback implements FallbackFactory<RmiSysNoticeFeign> {

    @Override
    public RmiSysNoticeFeign create(Throwable cause) {
        return new RmiSysNoticeFeign() {
            @Override
            public void send2Users(SendNoticeReq sendNoticeReq) {
                logPrintError(cause);
            }
        };
    }

}

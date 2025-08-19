package com.wzkris.system.rmi.fallback;

import com.wzkris.system.rmi.SysNoticeFeign;
import com.wzkris.system.rmi.domain.req.SendNoticeReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SysNoticeFeignFallback implements FallbackFactory<SysNoticeFeign> {

    @Override
    public SysNoticeFeign create(Throwable cause) {
        return new SysNoticeFeign() {
            @Override
            public void send2Users(SendNoticeReq sendNoticeReq) {
                log.error("send2Users => req: {}", sendNoticeReq, cause);
            }
        };
    }

}

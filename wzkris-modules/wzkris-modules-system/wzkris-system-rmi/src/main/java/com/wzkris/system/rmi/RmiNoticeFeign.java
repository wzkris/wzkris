package com.wzkris.system.rmi;

import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import com.wzkris.common.openfeign.core.RmiFeign;
import com.wzkris.system.rmi.domain.req.SendNoticeReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : RPC -- 通知服务
 * @since : 2024/12/16 12:55
 */
@FeignClient(name = ServiceIdConstant.SYSTEM, contextId = "RmiNoticeFeign")
public interface RmiNoticeFeign extends RmiFeign {

    String prefix = "/rmi_notice";

    /**
     * 发送通知
     */
    @PostMapping(prefix + "/send_notice")
    void sendNotice(SendNoticeReq sendNoticeReq);

}

package com.wzkris.user.rmi;

import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import com.wzkris.common.openfeign.core.RmiFeign;
import com.wzkris.user.rmi.domain.resp.OAuth2ClientResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - OAuth2客户端
 * @date : 2024/7/3 14:37
 */
@FeignClient(name = ServiceIdConstant.USER, contextId = "RmiOAuth2ClientFeign")
public interface RmiOAuth2ClientFeign extends RmiFeign {

    String prefix = "/rmi_oauth2_client";

    /**
     * 根据clientid查询客户端信息
     *
     * @param clientid clientid
     * @return oauth2客户端
     */
    @PostMapping(prefix + "/get_by_clientid")
    OAuth2ClientResp getByClientId(@RequestBody String clientid);

}

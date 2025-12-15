package com.wzkris.principal.httpservice.oauth2;

import com.wzkris.common.httpservice.annotation.HttpServiceClient;
import com.wzkris.common.httpservice.constants.ServiceIdConstant;
import com.wzkris.principal.httpservice.oauth2.fallback.OAuth2ClientHttpServiceFallback;
import com.wzkris.principal.httpservice.oauth2.resp.OAuth2ClientResp;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - OAuth2客户端
 * @date : 2024/7/3 14:37
 */
@HttpServiceClient(
        serviceId = ServiceIdConstant.PRINCIPAL,
        fallbackFactory = OAuth2ClientHttpServiceFallback.class
)
@HttpExchange(url = "/feign-oauth2-client")
public interface OAuth2ClientHttpService {

    /**
     * 根据id查询客户端信息
     *
     * @param id id
     * @return oauth2客户端
     */
    @PostExchange("/query-by-id")
    OAuth2ClientResp getById(@RequestBody String id);

    /**
     * 根据clientid查询客户端信息
     *
     * @param clientid clientid
     * @return oauth2客户端
     */
    @PostExchange("/query-by-clientid")
    OAuth2ClientResp getByClientId(@RequestBody String clientid);

}

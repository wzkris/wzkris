package com.wzkris.auth.httpclient.oauth2;

import com.wzkris.auth.httpclient.oauth2.resp.OAuth2ClientResp;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.httpclient.annotation.HttpClient;
import com.wzkris.common.httpclient.constants.ServiceContextPathConstant;
import com.wzkris.common.httpclient.constants.ServiceIdConstant;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - OAuth2客户端
 * @date : 2024/7/3 14:37
 */
@HttpClient(
        serviceId = ServiceIdConstant.USER_CENTER,
        path = ServiceContextPathConstant.USER_CENTER
)
@HttpExchange(url = "/oauth2-client")
public interface OAuth2ClientClient {

    /**
     * 根据id查询客户端信息
     *
     * @param id id
     * @return oauth2客户端
     */
    @PostExchange("/query-by-id")
    Result<OAuth2ClientResp> getById(@RequestBody String id);

    /**
     * 根据clientid查询客户端信息
     *
     * @param clientid clientid
     * @return oauth2客户端
     */
    @PostExchange("/query-by-clientid")
    Result<OAuth2ClientResp> getByClientId(@RequestBody String clientid);

}

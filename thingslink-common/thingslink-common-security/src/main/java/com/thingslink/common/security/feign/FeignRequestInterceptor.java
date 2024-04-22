package com.thingslink.common.security.feign;

import cn.hutool.core.util.ObjUtil;
import com.thingslink.common.core.constant.SecurityConstants;
import com.thingslink.common.core.utils.ServletUtil;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.core.utils.json.JsonUtil;
import com.thingslink.common.security.utils.CurrentUserHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : feign请求拦截器
 * @date : 2023/8/4 10:46
 */
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 通过feign调用的请求加上内部调用标识，否则无法访问feign暴露接口
        String target = requestTemplate.feignTarget().name();
        requestTemplate.header(SecurityConstants.INNER_REQUEST_HEADER, target);

        HttpServletRequest request = ServletUtil.getRequest();
        if (ObjUtil.isNotNull(request)) {
            String gatewayIp;
            if (request.getHeader(SecurityConstants.GATEWAY_IP_HEADER) == null) {
                // 如果网关IP请求头为空，则代表该request为网关转发
                gatewayIp = ServletUtil.getClientIP(request);
            }
            else {
                gatewayIp = request.getHeader(SecurityConstants.GATEWAY_IP_HEADER);
            }

            requestTemplate.header(SecurityConstants.GATEWAY_IP_HEADER, gatewayIp);
        }

        // 未经过认证则直接返回
        if (!CurrentUserHolder.isAuthenticated()) {
            return;
        }

        if (ObjUtil.isNotNull(request)) {
            // 追加Token
            String token = request.getHeader(SecurityConstants.TOKEN_HEADER);
            if (StringUtil.isNotBlank(token)) {
                requestTemplate.header(SecurityConstants.TOKEN_HEADER, token);
            }
        }

        // 追加个人信息
        requestTemplate.header(SecurityConstants.PRINCIPAL_HEADER, JsonUtil.toJsonString(CurrentUserHolder.getPrincipal()));
    }
}

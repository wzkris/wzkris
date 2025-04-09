package com.wzkris.user.api;

import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.user.api.domain.response.OAuth2ClientResp;
import com.wzkris.user.domain.OAuth2Client;
import com.wzkris.user.mapper.OAuth2ClientMapper;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : OAuth2客户端接口
 * @date : 2024/7/3 14:37
 */
@Service
@DubboService
@RequiredArgsConstructor
public class RemoteOAuth2ClientApiImpl implements RemoteOAuth2ClientApi {

    private final OAuth2ClientMapper oAuth2ClientMapper;

    @Override
    public OAuth2ClientResp getByClientId(String clientid) {
        OAuth2Client oAuth2Client = oAuth2ClientMapper.selectByClientId(clientid);
        return BeanUtil.convert(oAuth2Client, OAuth2ClientResp.class);
    }
}

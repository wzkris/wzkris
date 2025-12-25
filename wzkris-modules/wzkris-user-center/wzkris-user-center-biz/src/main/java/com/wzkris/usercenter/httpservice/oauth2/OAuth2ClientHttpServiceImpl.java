package com.wzkris.usercenter.httpservice.oauth2;

import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.domain.OAuth2ClientDO;
import com.wzkris.usercenter.httpservice.oauth2.resp.OAuth2ClientResp;
import com.wzkris.usercenter.mapper.OAuth2ClientMapper;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequiredArgsConstructor
public class OAuth2ClientHttpServiceImpl implements OAuth2ClientHttpService {

    private final OAuth2ClientMapper oAuth2ClientMapper;

    @Override
    public OAuth2ClientResp getById(String id) {
        OAuth2ClientDO oAuth2Client = oAuth2ClientMapper.selectById(id);
        return BeanUtil.convert(oAuth2Client, OAuth2ClientResp.class);
    }

    @Override
    public OAuth2ClientResp getByClientId(String clientid) {
        OAuth2ClientDO oAuth2Client = oAuth2ClientMapper.selectByClientId(clientid);
        return BeanUtil.convert(oAuth2Client, OAuth2ClientResp.class);
    }

}

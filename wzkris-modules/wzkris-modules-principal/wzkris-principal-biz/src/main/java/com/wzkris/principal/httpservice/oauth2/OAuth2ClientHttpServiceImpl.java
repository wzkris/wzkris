package com.wzkris.principal.httpservice.oauth2;

import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.principal.domain.OAuth2ClientDO;
import com.wzkris.principal.httpservice.oauth2.resp.OAuth2ClientResp;
import com.wzkris.principal.mapper.OAuth2ClientMapper;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequiredArgsConstructor
public class OAuth2ClientHttpServiceImpl implements OAuth2ClientHttpService {

    private final OAuth2ClientMapper oAuth2ClientMapper;

    @Override
    public OAuth2ClientResp getByClientId(String clientid) {
        OAuth2ClientDO oAuth2Client = oAuth2ClientMapper.selectByClientId(clientid);
        return BeanUtil.convert(oAuth2Client, OAuth2ClientResp.class);
    }

}

package com.wzkris.user.rmi;

import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.user.domain.OAuth2Client;
import com.wzkris.user.mapper.OAuth2ClientMapper;
import com.wzkris.user.rmi.domain.resp.OAuth2ClientResp;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequiredArgsConstructor
public class OAuth2ClientFeignImpl implements OAuth2ClientFeign {

    private final OAuth2ClientMapper oAuth2ClientMapper;

    @Override
    public OAuth2ClientResp getByClientId(String clientid) {
        OAuth2Client oAuth2Client = oAuth2ClientMapper.selectByClientId(clientid);
        return BeanUtil.convert(oAuth2Client, OAuth2ClientResp.class);
    }

}

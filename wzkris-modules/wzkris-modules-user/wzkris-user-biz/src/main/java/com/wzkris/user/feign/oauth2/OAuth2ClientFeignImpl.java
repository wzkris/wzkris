package com.wzkris.user.feign.oauth2;

import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.user.domain.OAuth2ClientDO;
import com.wzkris.user.feign.oauth2.resp.OAuth2ClientResp;
import com.wzkris.user.mapper.OAuth2ClientMapper;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/feign-oauth2-client")
@RequiredArgsConstructor
public class OAuth2ClientFeignImpl implements OAuth2ClientFeign {

    private final OAuth2ClientMapper oAuth2ClientMapper;

    @Override
    public OAuth2ClientResp getByClientId(String clientid) {
        OAuth2ClientDO oAuth2Client = oAuth2ClientMapper.selectByClientId(clientid);
        return BeanUtil.convert(oAuth2Client, OAuth2ClientResp.class);
    }

}

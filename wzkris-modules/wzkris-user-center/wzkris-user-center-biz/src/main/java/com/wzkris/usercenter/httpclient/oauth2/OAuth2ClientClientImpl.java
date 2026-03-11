package com.wzkris.usercenter.httpclient.oauth2;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.domain.OAuth2ClientDO;
import com.wzkris.usercenter.httpclient.oauth2.resp.OAuth2ClientResp;
import com.wzkris.usercenter.mapper.OAuth2ClientMapper;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequiredArgsConstructor
public class OAuth2ClientClientImpl implements OAuth2ClientClient {

    private final OAuth2ClientMapper oAuth2ClientMapper;

    @Override
    public Result<OAuth2ClientResp> getById(String id) {
        OAuth2ClientDO oAuth2Client = oAuth2ClientMapper.selectById(id);
        return Result.ok(BeanUtil.convert(oAuth2Client, OAuth2ClientResp.class));
    }

    @Override
    public Result<OAuth2ClientResp> getByClientId(String clientid) {
        OAuth2ClientDO oAuth2Client = oAuth2ClientMapper.selectByClientId(clientid);
        return Result.ok(BeanUtil.convert(oAuth2Client, OAuth2ClientResp.class));
    }

}

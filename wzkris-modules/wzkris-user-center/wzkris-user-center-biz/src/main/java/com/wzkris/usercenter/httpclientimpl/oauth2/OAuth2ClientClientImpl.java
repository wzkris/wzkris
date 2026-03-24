package com.wzkris.usercenter.httpclientimpl.oauth2;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.domain.OAuth2ClientDO;
import com.wzkris.usercenter.httpclientimpl.oauth2.resp.OAuth2ClientResp;
import com.wzkris.usercenter.mapper.OAuth2ClientMapper;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/oauth2-client")
@RequiredArgsConstructor
public class OAuth2ClientClientImpl {

    private final OAuth2ClientMapper oAuth2ClientMapper;

    @PostMapping("/query-by-id")
    public Result<OAuth2ClientResp> getById(String id) {
        OAuth2ClientDO oAuth2Client = oAuth2ClientMapper.selectById(id);
        return Result.ok(BeanUtil.convert(oAuth2Client, OAuth2ClientResp.class));
    }

    @PostMapping("/query-by-clientid")
    public Result<OAuth2ClientResp> getByClientId(String clientid) {
        OAuth2ClientDO oAuth2Client = oAuth2ClientMapper.selectByClientId(clientid);
        return Result.ok(BeanUtil.convert(oAuth2Client, OAuth2ClientResp.class));
    }

}

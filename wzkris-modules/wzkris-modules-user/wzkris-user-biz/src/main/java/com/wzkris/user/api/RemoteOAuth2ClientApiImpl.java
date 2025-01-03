package com.wzkris.user.api;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.common.openfeign.annotation.InnerAuth;
import com.wzkris.user.api.domain.response.OAuth2ClientResp;
import com.wzkris.user.domain.OAuth2Client;
import com.wzkris.user.mapper.OAuth2ClientMapper;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import static com.wzkris.common.core.domain.Result.ok;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : OAuth2客户端接口
 * @date : 2024/7/3 14:37
 */
@Hidden
@InnerAuth
@RestController
@RequiredArgsConstructor
public class RemoteOAuth2ClientApiImpl implements RemoteOAuth2ClientApi {
    private final OAuth2ClientMapper oAuth2ClientMapper;

    @Override
    public Result<OAuth2ClientResp> getByClientId(String clientid) {
        OAuth2Client oAuth2Client = oAuth2ClientMapper.selectByClientId(clientid);
        OAuth2ClientResp clientDTO = BeanUtil.convert(oAuth2Client, OAuth2ClientResp.class);
        return ok(clientDTO);
    }
}

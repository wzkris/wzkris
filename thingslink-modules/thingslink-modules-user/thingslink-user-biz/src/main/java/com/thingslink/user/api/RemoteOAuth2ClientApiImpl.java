package com.thingslink.user.api;

import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.utils.MapstructUtil;
import com.thingslink.common.security.annotation.InnerAuth;
import com.thingslink.user.api.domain.dto.OAuth2ClientDTO;
import com.thingslink.user.domain.OAuth2Client;
import com.thingslink.user.mapper.OAuth2ClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import static com.thingslink.common.core.domain.Result.success;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : OAuth2客户端接口
 * @date : 2024/7/3 14:37
 */
@InnerAuth
@RestController
@RequiredArgsConstructor
public class RemoteOAuth2ClientApiImpl implements RemoteOAuth2ClientApi {
    private final OAuth2ClientMapper oAuth2ClientMapper;

    @Override
    public Result<OAuth2ClientDTO> getByClientId(String clientid) {
        OAuth2Client oAuth2Client = oAuth2ClientMapper.selectByClientId(clientid);
        OAuth2ClientDTO clientDTO = MapstructUtil.convert(oAuth2Client, OAuth2ClientDTO.class);
        return success(clientDTO);
    }
}

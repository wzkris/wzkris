package com.wzkris.user.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.user.domain.OAuth2Client;
import com.wzkris.user.mapper.OAuth2ClientMapper;
import com.wzkris.user.service.OAuth2ClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * OAuth2服务
 *
 * @author wzkris
 */
@Service
@AllArgsConstructor
public class OAuth2ClientServiceImpl implements OAuth2ClientService {

    private final OAuth2ClientMapper oauth2ClientMapper;

    /**
     * 分页查询
     *
     * @param oAuth2Client 查询条件
     */
    @Override
    public List<OAuth2Client> list(OAuth2Client oAuth2Client) {
        // 查询
        LambdaQueryWrapper<OAuth2Client> lqw = this.buildQueryWrapper(oAuth2Client);
        return oauth2ClientMapper.selectList(lqw);
    }

    private LambdaQueryWrapper<OAuth2Client> buildQueryWrapper(OAuth2Client oAuth2Client) {
        return new LambdaQueryWrapper<OAuth2Client>()
                .eq(ObjUtil.isNotEmpty(oAuth2Client.getStatus()), OAuth2Client::getStatus, oAuth2Client.getStatus())
                .like(ObjUtil.isNotEmpty(oAuth2Client.getClientId()), OAuth2Client::getClientId, oAuth2Client.getClientId());
    }
}

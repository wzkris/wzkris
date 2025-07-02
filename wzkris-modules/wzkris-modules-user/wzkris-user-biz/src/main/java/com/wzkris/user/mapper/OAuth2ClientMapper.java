package com.wzkris.user.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.OAuth2Client;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuth2ClientMapper extends BaseMapperPlus<OAuth2Client> {

    default OAuth2Client selectByClientId(String clientId) {
        return this.selectOne(Wrappers.lambdaQuery(OAuth2Client.class).eq(OAuth2Client::getClientId, clientId));
    }

}

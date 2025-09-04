package com.wzkris.user.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.OAuth2ClientDO;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuth2ClientMapper extends BaseMapperPlus<OAuth2ClientDO> {

    default OAuth2ClientDO selectByClientId(String clientId) {
        return this.selectOne(Wrappers.lambdaQuery(OAuth2ClientDO.class).eq(OAuth2ClientDO::getClientId, clientId));
    }

}

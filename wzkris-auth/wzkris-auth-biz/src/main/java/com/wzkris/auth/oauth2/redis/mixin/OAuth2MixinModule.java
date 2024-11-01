package com.wzkris.auth.oauth2.redis.mixin;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wzkris.common.security.oauth2.domain.WzUser;
import com.wzkris.common.security.oauth2.domain.model.LoginApper;
import com.wzkris.common.security.oauth2.domain.model.LoginClient;
import com.wzkris.common.security.oauth2.domain.model.LoginSyser;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : OAuth2 json序列化白名单
 * @date : 2024/08/19 17:06
 */
public final class OAuth2MixinModule extends SimpleModule {

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(WzUser.class, WzUserMixin.class);
        context.setMixInAnnotations(LoginSyser.class, LoginSyserMixin.class);
        context.setMixInAnnotations(LoginApper.class, LoginApperMixin.class);
        context.setMixInAnnotations(LoginClient.class, LoginClientMixin.class);
    }
}

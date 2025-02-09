package com.wzkris.common.security.serialize;

import org.apache.dubbo.spring.security.jackson.ObjectMapperCodec;
import org.apache.dubbo.spring.security.jackson.ObjectMapperCodecCustomer;

public class OAuth2ObjectMapperCustomer implements ObjectMapperCodecCustomer {

    @Override
    public void customize(ObjectMapperCodec objectMapperCodec) {
        objectMapperCodec.addModule(new OAuth2CustomModule());
    }
}

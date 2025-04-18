/*
 * Copyright 2020-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wzkris.auth.oauth2.redis.convert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wzkris.auth.oauth2.utils.OAuth2JsonUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@ReadingConverter
public class BytesToUsernamePasswordAuthenticationTokenConverter
        implements Converter<byte[], UsernamePasswordAuthenticationToken> {

    private final Jackson2JsonRedisSerializer<UsernamePasswordAuthenticationToken> serializer;

    public BytesToUsernamePasswordAuthenticationTokenConverter() {
        ObjectMapper objectMapper = OAuth2JsonUtil.getObjectMapper();
        this.serializer = new Jackson2JsonRedisSerializer<>(objectMapper, UsernamePasswordAuthenticationToken.class);
    }

    @Override
    public UsernamePasswordAuthenticationToken convert(byte[] value) {
        return this.serializer.deserialize(value);
    }

}

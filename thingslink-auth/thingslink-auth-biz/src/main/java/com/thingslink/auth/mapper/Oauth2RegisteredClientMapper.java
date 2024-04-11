package com.thingslink.auth.mapper;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.thingslink.auth.domain.Oauth2RegisteredClient;
import com.thingslink.common.orm.plus.BaseMapperPlus;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Repository
public interface Oauth2RegisteredClientMapper extends BaseMapperPlus<Oauth2RegisteredClient> {

    @Results(id = "clientMap", value = {
            @Result(column = "scopes", property = "scopes",
                    jdbcType = JdbcType.ARRAY, typeHandler = JacksonTypeHandler.class),
            @Result(column = "authorization_grant_types", property = "authorizationGrantTypes",
                    jdbcType = JdbcType.ARRAY, typeHandler = JacksonTypeHandler.class),
            @Result(column = "redirect_uris", property = "redirectUris",
                    jdbcType = JdbcType.ARRAY, typeHandler = JacksonTypeHandler.class)
    })
    @Select("SELECT * FROM oauth2_registered_client WHERE client_id = #{clientId}")
    Oauth2RegisteredClient selectByClientId(String clientId);
}

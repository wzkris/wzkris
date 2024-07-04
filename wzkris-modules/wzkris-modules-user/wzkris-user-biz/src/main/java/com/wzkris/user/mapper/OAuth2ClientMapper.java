package com.wzkris.user.mapper;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.OAuth2Client;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuth2ClientMapper extends BaseMapperPlus<OAuth2Client> {

    @Results(id = "clientMap", value = {
            @Result(column = "scopes", property = "scopes",
                    jdbcType = JdbcType.ARRAY, typeHandler = JacksonTypeHandler.class),
            @Result(column = "authorization_grant_types", property = "authorizationGrantTypes",
                    jdbcType = JdbcType.ARRAY, typeHandler = JacksonTypeHandler.class),
            @Result(column = "redirect_uris", property = "redirectUris",
                    jdbcType = JdbcType.ARRAY, typeHandler = JacksonTypeHandler.class)
    })
    @Select("SELECT * FROM oauth2_client WHERE client_id = #{clientId}")
    OAuth2Client selectByClientId(String clientId);
}

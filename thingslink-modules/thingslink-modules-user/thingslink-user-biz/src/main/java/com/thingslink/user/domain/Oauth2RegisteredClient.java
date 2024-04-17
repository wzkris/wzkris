package com.thingslink.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.thingslink.common.orm.model.BaseEntity;
import com.thingslink.user.api.domain.dto.Oauth2ClientDTO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@AutoMappers({
        @AutoMapper(target = Oauth2ClientDTO.class),
})
@EqualsAndHashCode(callSuper = true)
@TableName(autoResultMap = true)
public class Oauth2RegisteredClient extends BaseEntity {
    @TableId
    private Long id;
    // 客户端id 等价于app_id
    private String clientId;
    // 客户端密钥 等价于app_secret
    private String clientSecret;
    // 权限域
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> scopes;
    // 授权类型
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> authorizationGrantTypes;
    // 回调地址
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> redirectUris;
    // 客户端状态
    private String status;
    // 是否自动放行
    private Boolean autoApprove;
}
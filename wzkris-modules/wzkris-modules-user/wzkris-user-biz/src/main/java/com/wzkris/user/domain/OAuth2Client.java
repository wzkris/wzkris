package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.user.api.domain.response.OAuth2ClientResp;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * OAuth2 表
 *
 * @author wzkris
 */
@Data
@AutoMappers({
        @AutoMapper(target = OAuth2ClientResp.class),
})
@TableName(autoResultMap = true, value = "oauth2_client")
public class OAuth2Client extends BaseEntity {

    @TableId
    private Long id;

    @NotBlank(message = "[clientName] {validate.notnull}")
    @Schema(description = "客户端名称")
    private String clientName;

    @Schema(description = "客户端状态")
    private String status;

    @NotBlank(message = "[clientId] {validate.notnull}")
    @Schema(description = "客户端id 等价于app_id")
    private String clientId;

    @NotBlank(message = "[clientSecret] {validate.notnull}")
    @Length(min = 6, max = 6, message = "[clientSecret] {validate.size.illegal}")
    @Schema(description = "客户端密钥 等价于app_secret")
    private String clientSecret;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "权限域")
    private List<String> scopes;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "授权类型")
    private List<String> authorizationGrantTypes;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "回调地址")
    private List<String> redirectUris;

    @Schema(description = "放行配置")
    private Boolean autoApprove;
}
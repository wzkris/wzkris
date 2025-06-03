package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.user.rmi.domain.resp.OAuth2ClientResp;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.ibatis.type.ArrayTypeHandler;

/**
 * OAuth2 表
 *
 * @author wzkris
 */
@Data
@AutoMappers({
    @AutoMapper(target = OAuth2ClientResp.class),
})
@TableName(schema = "biz", autoResultMap = true, value = "oauth2_client")
public class OAuth2Client extends BaseEntity {

    @TableId
    private Long id;

    @Schema(description = "客户端名称")
    private String clientName;

    @Schema(description = "客户端状态")
    private String status;

    @Schema(description = "客户端id 等价于app_id")
    private String clientId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "客户端密钥 等价于app_secret")
    private String clientSecret;

    @TableField(typeHandler = ArrayTypeHandler.class)
    @Schema(description = "权限域")
    private String[] scopes;

    @TableField(typeHandler = ArrayTypeHandler.class)
    @Schema(description = "授权类型")
    private String[] authorizationGrantTypes;

    @TableField(typeHandler = ArrayTypeHandler.class)
    @Schema(description = "回调地址")
    private String[] redirectUris;

    @Schema(description = "放行配置")
    private Boolean autoApprove;
}

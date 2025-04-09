package com.wzkris.user.domain.req;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wzkris.user.domain.OAuth2Client;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.apache.ibatis.type.ArrayTypeHandler;

@Data
@AutoMappers({@AutoMapper(target = OAuth2Client.class)})
@Schema(description = "客户端请求体")
public class OAuth2ClientReq {

    private Long id;

    @NotBlank(message = "{desc.client}{desc.name}{validate.notnull}")
    @Schema(description = "客户端名称")
    private String clientName;

    @Schema(description = "客户端状态")
    private String status;

    @NotBlank(message = "{desc.client}{desc.id}{validate.notnull}")
    @Schema(description = "客户端id 等价于app_id")
    private String clientId;

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

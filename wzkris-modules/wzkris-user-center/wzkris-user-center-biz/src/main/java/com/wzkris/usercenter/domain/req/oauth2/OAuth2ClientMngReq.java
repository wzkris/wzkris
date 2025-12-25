package com.wzkris.usercenter.domain.req.oauth2;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wzkris.usercenter.domain.OAuth2ClientDO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.apache.ibatis.type.ArrayTypeHandler;

@Data
@AutoMappers({@AutoMapper(target = OAuth2ClientDO.class)})
@Schema(description = "客户端请求体")
public class OAuth2ClientMngReq {

    private Long id;

    @NotBlank(message = "{invalidParameter.clientName.invalid}")
    @Schema(description = "客户端名称")
    private String clientName;

    @Schema(description = "客户端状态")
    private String status;

    @NotBlank(message = "{invalidParameter.id.invalid}")
    @Schema(description = "客户端id 等价于app_id")
    private String clientId;

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

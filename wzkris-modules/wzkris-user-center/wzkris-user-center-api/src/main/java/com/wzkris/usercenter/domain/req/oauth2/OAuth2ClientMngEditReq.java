package com.wzkris.usercenter.domain.req.oauth2;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wzkris.usercenter.domain.OAuth2ClientDO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.apache.ibatis.type.ArrayTypeHandler;

/**
 * 修改 OAuth2 客户端请求体
 */
@Data
@AutoMappers({@AutoMapper(target = OAuth2ClientDO.class)})
@Schema(description = "修改 OAuth2 客户端参数体")
public class OAuth2ClientMngEditReq {

    @NotNull(message = "{invalidParameter.id.invalid}")
    private Long id;

    @NotBlank(message = "{invalidParameter.clientName.invalid}")
    @Schema(description = "客户端名称")
    private String clientName;

    @Schema(description = "客户端状态")
    private String status;

    @NotBlank(message = "{invalidParameter.id.invalid}")
    @Schema(description = "客户端 id 等价于 app_id")
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

package com.wzkris.user.domain.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 修改客户端密钥
 */
@Data
public class EditClientSecretReq {

    @NotNull(message = "{invalidParameter.id.invalid}")
    private Long id;

    @NotBlank(message = "{invalidParameter.secret.invalid}")
    @Length(min = 6, max = 6, message = "{invalidParameter.secret.invalid}")
    private String secret;

}

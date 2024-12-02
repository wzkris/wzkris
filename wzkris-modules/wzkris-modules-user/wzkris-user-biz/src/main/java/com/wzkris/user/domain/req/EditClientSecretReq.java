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
    @NotNull(message = "[id] {validate.notnull}")
    private Long id;

    @NotBlank(message = "[secret] {validate.notnull}")
    @Length(min = 6, max = 6, message = "[secret] {validate.size.illegal}")
    private String secret;
}

package com.wzkris.auth.remote.interfaces.oauth2.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class OAuth2ClientQueryOneRequest implements Serializable {

    @Schema(description = "id")
    private String id;

    @Schema(description = "客户端id")
    private String clientId;

    @AssertTrue(message = "{invalidParameter.param.invalid}")
    public boolean hasQueryCondition() {
        return isNotBlank(id) || isNotBlank(clientId);
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

}

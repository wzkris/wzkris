package com.wzkris.usercenter.remote.api.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberQueryRequest implements Serializable {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "手机号")
    private String phoneNumber;

    @AssertTrue(message = "{invalidParameter.param.invalid}")
    public boolean hasQueryCondition() {
        return isNotBlank(username) || isNotBlank(phoneNumber);
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

}

package com.wzkris.user.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class AppUserQueryReq {

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "手机号码")
    private String phoneNumber;

    @Schema(description = "用户状态")
    private String status;

    @Schema(description = "请求参数")
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>(2);
        }
        return params;
    }
}

package com.wzkris.equipment.domain.dto;

import lombok.Data;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : emqx认证响应体
 * @date : 2024/4/13 14:13
 */
@Data
public class EmqxAuthResult {

    private String result = "ignore";

    private Boolean isSuperuser = false;

    /**
     * HTTP/1.1 200 OK
     * Headers: Content-Type: application/json
     * ...
     * Body:
     * {
     *     "result": "allow", // 可选 "allow" | "deny" | "ignore"
     *     "is_superuser": true // 可选 true | false，该项为空时默认为 false
     * }
     */
}

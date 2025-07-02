package com.wzkris.common.captcha.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * token 响应
 *
 * @author wuhunyu
 * @date 2025/06/16 18:34
 **/
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedeemChallengeResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 2634742622684976147L;

    private Boolean success;

    private String message;

    private String token;

    private Date expires;

}

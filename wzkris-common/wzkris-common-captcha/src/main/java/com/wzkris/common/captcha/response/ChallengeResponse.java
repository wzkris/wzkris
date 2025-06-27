package com.wzkris.common.captcha.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 挑战响应
 *
 * @author wuhunyu
 * @date 2025/06/16 21:24
 **/
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 6524572173531919368L;

    private List<List<String>> challenge;

    private Date expires;

    private String token;

}

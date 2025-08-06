package com.wzkris.common.captcha.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 挑战
 *
 * @author wuhunyu
 * @date 2025/06/16 16:14
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeData implements Serializable {

    @Serial
    private static final long serialVersionUID = -2996283225878621851L;

    private Challenge challenge;

    private Date expires;

    private String token;

}

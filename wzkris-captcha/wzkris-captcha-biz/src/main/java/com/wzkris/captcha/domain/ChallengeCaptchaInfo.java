package com.wzkris.captcha.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeCaptchaInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = -2996283225878621851L;

    private Challenge challenge;

    private Date expires;

    private String token;

}

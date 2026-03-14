package com.wzkris.risk.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

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

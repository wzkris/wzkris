package com.wzkris.common.captcha.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * token
 *
 * @author wuhunyu
 * @date 2025/06/16 18:01
 **/
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token implements Serializable {

    @Serial
    private static final long serialVersionUID = -6931868214117599084L;

    private String token;

    private Date expires;

}

package com.wzkris.common.captcha.model;

import java.util.Date;

/**
 * token
 *
 * @author wuhunyu
 * @date 2025/06/16 18:01
 **/
public record Token(String token, Date expires) {

}

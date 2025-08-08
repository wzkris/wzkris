package com.wzkris.common.captcha.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 兑换调整请求
 *
 * @author wuhunyu
 * @date 2025/06/16 18:31
 **/
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedeemChallengeRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -8683647715596989623L;

    private String token;

    private List<Integer> solutions;

}

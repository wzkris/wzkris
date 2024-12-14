package com.wzkris.user.api.domain.response;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * app用户传输层
 *
 * @author wzkris
 */
@Data
@Accessors(chain = true)
@FieldNameConstants
public class AppUserResp {

    private Long userId;

    private String nickname;

    private String phoneNumber;

    private String status;
}

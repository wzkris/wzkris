package com.wzkris.user.api.domain.response;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

/**
 * app用户传输层
 *
 * @author wzkris
 */
@Data
@FieldNameConstants
public class AppUserResp implements Serializable {

    private Long userId;

    private String nickname;

    private String phoneNumber;

    private String status;
}

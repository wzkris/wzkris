package com.wzkris.user.rmi.domain.resp;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

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

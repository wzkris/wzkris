package com.wzkris.user.api.domain.dto;

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
public class CustomerDTO {

    private Long userId;

    private String nickname;

    private String phoneNumber;

    private String status;

    private String gender;

    private String avatar;
}

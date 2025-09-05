package com.wzkris.user.feign.customer.resp;

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
public class CustomerResp implements Serializable {

    private Long customerId;

    private String nickname;

    private String phoneNumber;

    private String status;

}

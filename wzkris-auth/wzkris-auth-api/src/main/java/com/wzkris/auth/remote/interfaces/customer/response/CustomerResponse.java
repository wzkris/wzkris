package com.wzkris.auth.remote.interfaces.customer.response;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

/**
 * 客户传输层
 *
 * @author wzkris
 */
@Data
@FieldNameConstants
public class CustomerResponse implements Serializable {

    private Long id;

    private String nickname;

    private String phoneNumber;

    private String status;

}


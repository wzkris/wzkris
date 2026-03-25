package com.wzkris.usercenter.remote.api.customer.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerResponse implements Serializable {

    private Long customerId;

    private String nickname;

    private String phoneNumber;

    private String status;

}

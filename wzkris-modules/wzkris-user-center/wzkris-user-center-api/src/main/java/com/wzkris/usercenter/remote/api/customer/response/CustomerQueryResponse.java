package com.wzkris.usercenter.remote.api.customer.response;

import com.wzkris.usercenter.enums.customer.CustomerStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CustomerQueryResponse implements Serializable {

    private Long id;

    private String nickname;

    private String phoneNumber;

    private CustomerStatusEnum status;

}

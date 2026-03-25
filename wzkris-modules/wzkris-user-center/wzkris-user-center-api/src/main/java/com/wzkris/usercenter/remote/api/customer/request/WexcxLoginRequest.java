package com.wzkris.usercenter.remote.api.customer.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WexcxLoginRequest {

    private String identifier;

    private String phoneNumber;

}

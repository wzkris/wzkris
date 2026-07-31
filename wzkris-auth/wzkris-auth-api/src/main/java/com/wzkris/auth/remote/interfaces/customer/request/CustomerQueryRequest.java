package com.wzkris.auth.remote.interfaces.customer.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerQueryRequest implements Serializable {

    @Schema(description = "手机号")
    private String phoneNumber;

}

package com.wzkris.gateway.remote.interfaces.loginuser.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserQueryRequest implements Serializable {

    private String authType;

    private Long uid;

    private String sid;

}


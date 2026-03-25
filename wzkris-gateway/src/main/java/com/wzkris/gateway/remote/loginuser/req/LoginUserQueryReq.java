package com.wzkris.gateway.remote.loginuser.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserQueryReq implements Serializable {

    private String authType;

    private Long uid;

    private String sid;

}

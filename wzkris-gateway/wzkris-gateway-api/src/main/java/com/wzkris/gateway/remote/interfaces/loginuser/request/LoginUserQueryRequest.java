package com.wzkris.gateway.remote.interfaces.loginuser.request;

import com.wzkris.common.core.enums.AuthTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserQueryRequest implements Serializable {

    private AuthTypeEnum authType;

    private Long uid;

    private String sid;

}


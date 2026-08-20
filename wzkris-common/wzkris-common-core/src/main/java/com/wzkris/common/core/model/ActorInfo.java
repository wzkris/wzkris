package com.wzkris.common.core.model;

import com.wzkris.common.core.enums.AuthTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 代操作（impersonation）实际操作者信息。
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ActorInfo implements Serializable {

    private Long uid;

    private AuthTypeEnum authType;

    private String sid;

    public static ActorInfo of(Long uid, AuthTypeEnum authType, String sid) {
        return new ActorInfo(uid, authType, sid);
    }

}

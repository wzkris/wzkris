package com.wzkris.usercenter.httpclient.member.req;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberPermsQueryReq implements Serializable {

    @Nonnull
    private Long memberId;

    @Nonnull
    private Long tenantId;

}

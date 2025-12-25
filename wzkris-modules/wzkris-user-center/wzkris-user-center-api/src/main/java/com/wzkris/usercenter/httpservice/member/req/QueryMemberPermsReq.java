package com.wzkris.usercenter.httpservice.member.req;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryMemberPermsReq implements Serializable {

    @Nonnull
    private Long memberId;

    @Nonnull
    private Long tenantId;

}

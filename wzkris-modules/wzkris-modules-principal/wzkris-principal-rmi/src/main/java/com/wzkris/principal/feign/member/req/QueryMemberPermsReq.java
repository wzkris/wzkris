package com.wzkris.principal.feign.member.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryMemberPermsReq implements Serializable {

    @NonNull
    private Long memberId;

    @NonNull
    private Long tenantId;

}

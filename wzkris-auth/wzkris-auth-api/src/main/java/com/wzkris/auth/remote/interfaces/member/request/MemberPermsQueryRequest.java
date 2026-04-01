package com.wzkris.auth.remote.interfaces.member.request;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberPermsQueryRequest implements Serializable {

    @Nonnull
    private Long memberId;

    @Nonnull
    private Long tenantId;

}


package com.wzkris.usercenter.remote.api.member.request;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantIdRequest implements Serializable {

    @Nonnull
    private Long tenantId;

}

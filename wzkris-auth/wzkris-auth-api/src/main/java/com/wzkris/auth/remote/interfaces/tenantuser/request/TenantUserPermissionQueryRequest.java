package com.wzkris.auth.remote.interfaces.tenantuser.request;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantUserPermissionQueryRequest implements Serializable {

    @Nonnull
    private Long id;

    @Nonnull
    private Long tenantId;

}


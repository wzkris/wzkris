package com.wzkris.principal.httpservice.admin.req;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryAdminPermsReq implements Serializable {

    @Nonnull
    private Long adminId;

    @Nullable
    private Long deptId;

}

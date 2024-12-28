package com.wzkris.user.api.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryPermsReq {

    @NonNull
    private Long userId;

    @NonNull
    private Long tenantId;

    @Nullable
    private Long deptId;
}

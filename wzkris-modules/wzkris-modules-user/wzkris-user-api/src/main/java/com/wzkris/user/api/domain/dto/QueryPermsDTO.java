package com.wzkris.user.api.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
public class QueryPermsDTO {
    @NonNull
    private Long userId;
    @NonNull
    private Long tenantId;
    @Nullable
    private Long deptId;

    public QueryPermsDTO() {
    }
}

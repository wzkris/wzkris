package com.wzkris.user.rmi.domain.req;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryPermsReq implements Serializable {

    @NonNull
    private Long userId;

    @NonNull
    private Long tenantId;

    @Nullable
    private Long deptId;
}

package com.wzkris.principal.httpservice.admin.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryAdminPermsReq implements Serializable {

    @NonNull
    private Long adminId;

    @Nullable
    private Long deptId;

}

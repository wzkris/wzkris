package com.wzkris.principal.feign.staff.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryStaffPermsReq implements Serializable {

    @NonNull
    private Long userId;

    @NonNull
    private Long tenantId;

}

package com.wzkris.principal.feign.userinfo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryUserPermsReq implements Serializable {

    @NonNull
    private Long userId;

    @Nullable
    private Long deptId;

}

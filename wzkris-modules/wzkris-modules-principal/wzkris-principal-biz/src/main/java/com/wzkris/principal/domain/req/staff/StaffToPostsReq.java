package com.wzkris.principal.domain.req.staff;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 员工对职位 一对多
 */
@Data
public class StaffToPostsReq {

    @NotNull(message = "{invalidParameter.id.invalid}")
    private Long staffId;

    private List<Long> postIds;

}

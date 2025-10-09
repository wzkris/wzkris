package com.wzkris.user.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 员工和职位关联表
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffToPostDO {

    @Schema(description = "员工ID")
    private Long staffId;

    @Schema(description = "职位ID")
    private Long postId;

}

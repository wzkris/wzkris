package com.thingslink.user.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户和岗位关联 sys_user_post
 *
 * @author wzkris
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SysUserPost {
    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "岗位ID")
    private Long postId;
}

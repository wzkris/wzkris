package com.wzkris.usercenter.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 职位和菜单关联表 post_to_menu
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostToMenuDO {

    @Schema(description = "职位ID")
    private Long postId;

    @Schema(description = "菜单ID")
    private Long menuId;

}

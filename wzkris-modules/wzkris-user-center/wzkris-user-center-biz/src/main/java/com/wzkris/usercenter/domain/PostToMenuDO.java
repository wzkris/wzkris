package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 职位和菜单关联表 post_to_menu
 *
 * @author wzkris
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(schema = "biz", value = "post_to_menu", autoResultMap = true)
public class PostToMenuDO extends BaseEntity {

    @Schema(description = "职位ID")
    private Long postId;

    @Schema(description = "菜单ID")
    private Long menuId;

}

package com.wzkris.user.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 菜单节点选择树
 */
@Data
public class SysMenuCheckSelectTreeVO {
    @Schema(description = "已选中节点")
    private List<Long> checkedKeys;

    @Schema(description = "可选择菜单树")
    private List<SelectTreeVO> menus;
}

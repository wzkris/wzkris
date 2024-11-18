package com.wzkris.user.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 部门节点选择树
 */
@Data
public class SysDeptCheckSelectTreeVO {

    @Schema(description = "已选中节点")
    private List<Long> checkedKeys;

    @Schema(description = "可选择部门树")
    private List<SelectTreeVO> depts;
}

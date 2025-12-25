package com.wzkris.usercenter.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 节点选择树
 */
@Data
public class CheckedSelectVO {

    @Schema(description = "已选中节点")
    private List<Long> checkedKeys;

    @Schema(description = "可选择列表")
    private List<SelectVO> selects;

}

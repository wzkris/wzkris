package com.wzkris.user.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

/**
 * 节点选择树
 */
@Data
public class CheckedSelectVO {

    @Schema(description = "已选中节点")
    private List<Long> checkedKeys;

    @Schema(description = "可选择树")
    private List<SelectVO> selects;
}

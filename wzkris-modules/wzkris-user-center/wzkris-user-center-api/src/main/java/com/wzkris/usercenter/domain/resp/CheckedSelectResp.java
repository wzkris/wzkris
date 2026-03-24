package com.wzkris.usercenter.domain.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 节点选择树
 */
@Data
public class CheckedSelectResp {

    @Schema(description = "已选中节点")
    private List<Long> checkedKeys;

    @Schema(description = "可选择列表")
    private List<SelectResp> selects;

}

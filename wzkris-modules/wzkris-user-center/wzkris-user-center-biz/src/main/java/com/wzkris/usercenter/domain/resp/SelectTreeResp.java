package com.wzkris.usercenter.domain.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wzkris.usercenter.domain.DeptInfoDO;
import com.wzkris.usercenter.domain.MenuInfoDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SelectTree树结构实体类
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class SelectTreeResp implements Serializable {

    @Serial
    private static final long serialVersionUID = -3890661499660226052L;

    @Schema(description = "节点ID")
    private Long id;

    @Schema(description = "节点名称")
    private String label;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Schema(description = "子节点")
    private List<SelectTreeResp> children;

    public SelectTreeResp(DeptInfoDO dept) {
        this.id = dept.getDeptId();
        this.label = dept.getDeptName();
        this.children = dept.getChildren().stream().map(SelectTreeResp::new).collect(Collectors.toList());
    }

    public SelectTreeResp(MenuInfoDO menu) {
        this.id = menu.getMenuId();
        this.label = menu.getMenuName();
        this.children = menu.getChildren().stream().map(SelectTreeResp::new).collect(Collectors.toList());
    }

}

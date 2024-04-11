package com.thingslink.user.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.thingslink.user.domain.SysMenu;
import com.thingslink.user.domain.SysDept;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
@NoArgsConstructor
public class SelectTree implements Serializable {

    @Serial
    private static final long serialVersionUID = -3890661499660226052L;

    @Schema(description = "节点ID")
    private Long id;

    @Schema(description = "节点名称")
    private String label;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Schema(description = "子节点")
    private List<SelectTree> children;

    public SelectTree(SysDept dept) {
        this.id = dept.getDeptId();
        this.label = dept.getDeptName();
        this.children = dept.getChildren().stream().map(SelectTree::new).collect(Collectors.toList());
    }

    public SelectTree(SysMenu menu) {
        this.id = menu.getMenuId();
        this.label = menu.getMenuName();
        this.children = menu.getChildren().stream().map(SelectTree::new).collect(Collectors.toList());
    }

}

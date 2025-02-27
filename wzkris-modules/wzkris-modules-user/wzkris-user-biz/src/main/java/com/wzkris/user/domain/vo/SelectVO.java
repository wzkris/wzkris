package com.wzkris.user.domain.vo;

import com.wzkris.user.domain.SysPost;
import com.wzkris.user.domain.SysRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * Select选择结构实体类
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class SelectVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7304244785923554056L;

    @Schema(description = "节点ID")
    private Long id;

    @Schema(description = "节点名称")
    private String label;

    public SelectVO(SysRole role) {
        this.id = role.getRoleId();
        this.label = role.getRoleName();
    }

    public SelectVO(SysPost post) {
        this.id = post.getPostId();
        this.label = post.getPostName();
    }
}

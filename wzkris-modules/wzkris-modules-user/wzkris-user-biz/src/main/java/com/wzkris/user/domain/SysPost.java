package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 岗位表 post
 *
 * @author wzkris
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class SysPost extends BaseEntity {


    @TableId
    private Long postId;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Size(min = 0, max = 30, message = "[postCode] {validate.size.illegal}")
    @Schema(description = "岗位编码")
    private String postCode;

    @NotBlank(message = "[postName] {validate.notnull}")
    @Size(min = 0, max = 30, message = "[postName] {validate.size.illegal}")
    @Schema(description = "岗位名称")
    private String postName;

    @Schema(description = "状态（0正常 1停用）")
    private String status;

    @NotNull(message = "[postSort] {validate.notnull}")
    @Schema(description = "岗位排序")
    private Integer postSort;

    public SysPost(Long postId) {
        this.postId = postId;
    }

    public SysPost(String status) {
        this.status = status;
    }
}

package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 岗位表 sys_post
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz_sys")
public class SysPost extends BaseEntity {

    @TableId
    private Long postId;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "岗位编码")
    private String postCode;

    @Schema(description = "岗位名称")
    private String postName;

    @Schema(description = "状态（0正常 1停用）")
    private String status;

    @Schema(description = "岗位排序")
    private Integer postSort;

    public SysPost(Long postId) {
        this.postId = postId;
    }

    public SysPost(String status) {
        this.status = status;
    }
}

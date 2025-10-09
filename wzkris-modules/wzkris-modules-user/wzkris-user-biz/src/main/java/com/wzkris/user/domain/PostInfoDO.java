package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 员工职位表 t_post_info
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz", value = "t_post_info")
public class PostInfoDO extends BaseEntity {

    @TableId
    private Long postId;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "职位名称")
    private String postName;

    @Schema(description = "状态（0代表正常 1代表停用）")
    private String status;

    @Schema(description = "角色排序")
    private Integer postSort;

    public PostInfoDO(Long postId) {
        this.postId = postId;
    }

    public PostInfoDO(String status) {
        this.status = status;
    }

}

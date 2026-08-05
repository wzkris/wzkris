package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.usercenter.enums.post.PostStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 租户职位表 post_info
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz", value = "post_info")
public class PostInfoDO extends BaseEntity {

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "职位名称")
    private String postName;

    @Schema(description = "状态（0代表正常 1代表停用）")
    private PostStatusEnum status;

    @Schema(description = "角色排序")
    private Integer postSort;

    public PostInfoDO(Long id) {
        this.setId(id);
    }

    public PostInfoDO(PostStatusEnum status) {
        this.status = status;
    }

}


package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 租户成员和职位关联表
 *
 * @author wzkris
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(schema = "biz", value = "member_to_post", autoResultMap = true)
public class MemberToPostDO extends BaseEntity {

    @Schema(description = "成员ID")
    private Long memberId;

    @Schema(description = "职位ID")
    private Long postId;

}

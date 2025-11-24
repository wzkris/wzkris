package com.wzkris.principal.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.principal.httpservice.member.resp.MemberInfoResp;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 租户成员对象
 *
 * @author wzkris
 */
@Data
@AutoMappers({@AutoMapper(target = MemberInfoResp.class)})
@NoArgsConstructor
@TableName(schema = "biz", value = "member_info")
public class MemberInfoDO extends BaseEntity {

    @TableId
    private Long memberId;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "手机号码")
    private String phoneNumber;

    @Schema(description = "状态值")
    private String status;

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "头像")
    private String avatar;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "密码")
    private String password;

    @Schema(description = "最近登录ip")
    private String loginIp;

    @Schema(description = "最近登录日期")
    private Date loginDate;

    @Schema(description = "额外信息")
    private String remark;

    public MemberInfoDO(Long memberId) {
        this.memberId = memberId;
    }

}

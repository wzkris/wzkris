package com.wzkris.principal.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.principal.feign.staff.resp.StaffInfoResp;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 租户员工对象
 *
 * @author wzkris
 */
@Data
@AutoMappers({@AutoMapper(target = StaffInfoResp.class)})
@NoArgsConstructor
@TableName(schema = "biz", value = "t_staff_info")
public class StaffInfoDO extends BaseEntity {

    @TableId
    private Long staffId;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "员工账号")
    private String staffName;

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

    public StaffInfoDO(Long staffId) {
        this.staffId = staffId;
    }

}

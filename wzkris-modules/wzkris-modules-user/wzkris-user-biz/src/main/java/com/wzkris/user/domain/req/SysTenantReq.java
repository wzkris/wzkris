package com.wzkris.user.domain.req;

import com.wzkris.common.core.annotation.Xss;
import com.wzkris.common.core.annotation.group.ValidationGroups;
import com.wzkris.user.domain.SysTenant;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 租户传输层
 */
@Data
@AutoMappers({@AutoMapper(target = SysTenant.class)})
@Schema(description = "系统用户添加修改参数体")
public class SysTenantReq {

    private Long tenantId;

    @NotBlank(message = "{desc.tenant}{desc.type}{validate.notnull}")
    @Schema(description = "租户类型 0-个人 1-企业")
    private String tenantType;

    @Schema(description = "联系电话")
    private String contactPhone;

    @NotBlank(message = "{desc.tenant}{desc.name}{validate.notnull}")
    @Schema(description = "租户名称")
    private String tenantName;

    @Schema(description = "租户状态")
    private String status;

    @Schema(description = "域名")
    private String domain;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "租户套餐编号")
    private Long packageId;

    @NotNull(message = "{desc.expireTime}{validate.notnull}")
    @Schema(description = "过期时间")
    private Date expireTime;

    @NotNull(message = "账号数量{validate.notnull}")
    @Schema(description = "账号数量（-1不限制）")
    private Integer accountLimit;

    @NotNull(message = "角色数量{validate.notnull}")
    @Schema(description = "角色数量（-1不限制）")
    private Integer roleLimit;

    @NotNull(message = "岗位数量{validate.notnull}")
    @Schema(description = "岗位数量（-1不限制）")
    private Integer postLimit;

    @NotNull(message = "部门数量{validate.notnull}")
    @Schema(description = "部门数量（-1不限制）")
    private Integer deptLimit;

    // 用户名只能为小写英文、数字和下划线
    @Pattern(
            regexp = "^[a-z0-9_]+$",
            message = "{desc.username}{validate.illegal}",
            groups = ValidationGroups.Insert.class)
    @Xss(message = "{desc.user}{desc.name}" + "{validate.xss.forbid}", groups = ValidationGroups.Insert.class)
    @NotBlank(message = "{desc.login}{desc.username}{validate.notnull}", groups = ValidationGroups.Insert.class)
    @Schema(description = "登录用户名")
    private String username;

}

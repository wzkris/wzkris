package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门表 sys_dept
 *
 * @author wzkris
 */
@Data
@ToString
@Accessors(chain = true)
@NoArgsConstructor
public class SysDept extends BaseEntity {


    @TableId
    private Long deptId;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "父部门ID")
    private Long parentId;

    @Schema(description = "祖级列表")
    private String ancestors;

    @NotBlank(message = "[deptName] {validate.notnull}")
    @Size(min = 2, max = 30, message = "[deptName] {validate.size.illegal}")
    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "0代表存在 1代表停用")
    private String status;

    @NotNull(message = "[deptSort] {validate.notnull}")
    @Schema(description = "显示顺序")
    private Integer deptSort;

    @Size(min = 0, max = 15, message = "[contact] {validate.size.illegal}")
    @Schema(description = "联系电话")
    private String contact;

    @Email(message = "{validate.email.illegal}")
    @Size(min = 0, max = 50, message = "[email] {validate.size.illegal}")
    @Schema(description = "部门邮箱")
    private String email;

    @TableField(exist = false)
    @Schema(description = "子部门")
    private List<SysDept> children = new ArrayList<>();

    public SysDept(Long deptId) {
        this.deptId = deptId;
    }

}

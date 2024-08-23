package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wzkris.common.orm.model.BaseEntity;
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
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 父部门ID
     */
    private Long parentId;
    /**
     * 祖级列表
     */
    private String ancestors;
    /**
     * 部门名称
     */
    @NotBlank(message = "[deptName] {validate.notnull}")
    @Size(min = 0, max = 30, message = "[deptName] {validate.size.illegal}")
    private String deptName;
    /**
     * 0代表存在 1代表停用
     */
    private String status;
    /**
     * 显示顺序
     */
    @NotNull(message = "[deptSort] {validate.notnull}")
    private Integer deptSort;
    /**
     * 联系电话
     */
    @Size(min = 0, max = 15, message = "[contact] {validate.size.illegal}")
    private String contact;
    /**
     * 邮箱
     */
    @Email(message = "{validate.email.illegal}")
    @Size(min = 0, max = 50, message = "[email] {validate.size.illegal}")
    private String email;
    /**
     * 子部门
     */
    @TableField(exist = false)
    private List<SysDept> children = new ArrayList<>();

    public SysDept(Long deptId) {
        this.deptId = deptId;
    }

    public SysDept(String status) {
        this.status = status;
    }
}

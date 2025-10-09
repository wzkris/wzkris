package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.ArrayTypeHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门表 dept_info
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz", value = "dept_info", autoResultMap = true)
public class DeptInfoDO extends BaseEntity {

    @TableId
    private Long deptId;

    @Schema(description = "父部门ID")
    private Long parentId;

    @TableField(typeHandler = ArrayTypeHandler.class)
    @Schema(description = "祖级列表")
    private Long[] ancestors;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "0代表存在 1代表停用")
    private String status;

    @Schema(description = "显示顺序")
    private Integer deptSort;

    @Schema(description = "联系电话")
    private String contact;

    @Schema(description = "部门邮箱")
    private String email;

    @TableField(exist = false)
    @Schema(description = "子部门")
    private List<DeptInfoDO> children = new ArrayList<>();

    public DeptInfoDO(Long deptId) {
        this.deptId = deptId;
    }

}

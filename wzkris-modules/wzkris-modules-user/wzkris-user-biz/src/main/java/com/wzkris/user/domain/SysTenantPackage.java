package com.wzkris.user.domain;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 租户套餐表 sys_tenant_package
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(autoResultMap = true)
public class SysTenantPackage extends BaseEntity {

    @TableId
    private Long packageId;

    @NotBlank(message = "[packageName] {validate.notnull}")
    @Schema(description = "套餐名称")
    private String packageName;

    @Schema(description = "状态（0正常 1停用）")
    private String status;

    @Schema(description = "套餐绑定的菜单")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> menuIds;

    @Schema(description = "菜单树选择项是否关联显示")
    private Boolean isMenuDisplay;

    @Schema(description = "备注")
    private String remark;

}

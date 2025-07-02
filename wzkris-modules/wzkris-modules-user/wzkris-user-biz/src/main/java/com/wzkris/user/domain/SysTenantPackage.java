package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.ArrayTypeHandler;

/**
 * 租户套餐表 sys_tenant_package
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(schema = "biz", autoResultMap = true)
public class SysTenantPackage extends BaseEntity {

    @TableId
    private Long packageId;

    @Schema(description = "套餐名称")
    private String packageName;

    @Schema(description = "状态（0正常 1停用）")
    private String status;

    @TableField(typeHandler = ArrayTypeHandler.class)
    @Schema(description = "套餐绑定的菜单")
    private Long[] menuIds;

    @Schema(description = "备注")
    private String remark;

    public SysTenantPackage(Long packageId) {
        this.packageId = packageId;
    }

}

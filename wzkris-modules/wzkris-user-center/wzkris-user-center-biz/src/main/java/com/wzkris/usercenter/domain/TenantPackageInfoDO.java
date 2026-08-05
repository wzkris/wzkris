package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.usercenter.enums.tenantpackage.TenantPackageStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.ArrayTypeHandler;

/**
 * 租户套餐表
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(schema = "biz", value = "tenant_package_info", autoResultMap = true)
public class TenantPackageInfoDO extends BaseEntity {

    @Schema(description = "套餐名称")
    private String packageName;

    @Schema(description = "状态")
    private TenantPackageStatusEnum status;

    @TableField(typeHandler = ArrayTypeHandler.class)
    @Schema(description = "套餐绑定的菜单")
    private Long[] menuIds;

    @Schema(description = "账号数量（-1不限制）")
    private Integer memberNumLimit;

    @Schema(description = "职位数量（-1不限制）")
    private Integer postNumLimit;

    @Schema(description = "备注")
    private String remark;

    public TenantPackageInfoDO(Long id) {
        this.setId(id);
    }

}


package com.wzkris.usercenter.domain.resp;

import com.wzkris.usercenter.domain.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * Select选择结构实体类
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class SelectResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 7304244785923554056L;

    @Schema(description = "节点ID")
    private Long id;

    @Schema(description = "节点名称")
    private String label;

    public SelectResp(AdminInfoDO admin) {
        this.id = admin.getAdminId();
        this.label = admin.getUsername();
    }

    public SelectResp(RoleInfoDO role) {
        this.id = role.getRoleId();
        this.label = role.getRoleName();
    }

    public SelectResp(TenantPackageInfoDO tenantPackage) {
        this.id = tenantPackage.getPackageId();
        this.label = tenantPackage.getPackageName();
    }

    public SelectResp(TenantInfoDO tenant) {
        this.id = tenant.getTenantId();
        this.label = tenant.getTenantName();
    }

    public SelectResp(PostInfoDO post) {
        this.id = post.getPostId();
        this.label = post.getPostName();
    }

}

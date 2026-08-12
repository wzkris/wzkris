package com.wzkris.usercenter.api.tenantuser.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户分页列表元素响应（Mng 轨集合元素 -> {域}MngPageResponse）
 *
 * <p>继承 {@link TenantUserMngQueryResponse}，补充列表展示所需的角色名称跨表字段。
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TenantUserMngPageResponse extends TenantUserMngQueryResponse {

    @Schema(description = "角色名称")
    private String roleName;

}

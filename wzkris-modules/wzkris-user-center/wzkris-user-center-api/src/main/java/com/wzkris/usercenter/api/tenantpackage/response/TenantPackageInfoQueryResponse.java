package com.wzkris.usercenter.api.tenantpackage.response;

import com.wzkris.usercenter.enums.tenantpackage.TenantPackageStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 租户套餐概览
 */
@Data
public class TenantPackageInfoQueryResponse {

    @Schema(description = "是否已分配套餐")
    private Boolean packageAssigned;

    @Schema(description = "租户名称")
    private String tenantName;

    @Schema(description = "套餐ID")
    private Long id;

    @Schema(description = "套餐名称")
    private String packageName;

    @Schema(description = "套餐状态（0正常 1停用）")
    private TenantPackageStatusEnum packageStatus;

    @Schema(description = "套餐说明")
    private String packageRemark;

    @Schema(description = "到期时间")
    private OffsetDateTime expireTime;

    @Schema(description = "续费状态 active | expiring_soon | expired | inactive | permanent")
    private String renewalStatus;

    @Schema(description = "配额列表")
    private List<QuotaItem> quotaItems = new ArrayList<>();

    @Schema(description = "权益列表")
    private List<BenefitItem> benefitItems = new ArrayList<>();

    // ============ 内部类 ============

    /**
     * 配额项 — 只有业务数据：标识、已用、上限
     */
    @Data
    public static class QuotaItem {

        @Schema(description = "配额标识 account | role | ...")
        private String quotaKey;

        @Schema(description = "已使用")
        private Integer used;

        @Schema(description = "总配额，-1 表示无限制")
        private Integer limit;

        @Schema(description = "是否无限制")
        private Boolean unlimited;

    }

    /**
     * 权益项 — 菜单名称
     */
    @Data
    public static class BenefitItem {

        @Schema(description = "权益标识（菜单ID）")
        private String benefitKey;

        @Schema(description = "权益标题（菜单名称）")
        private String title;

    }

}

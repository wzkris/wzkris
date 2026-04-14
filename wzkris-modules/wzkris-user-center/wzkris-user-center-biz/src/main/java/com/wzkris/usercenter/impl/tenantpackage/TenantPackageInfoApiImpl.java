package com.wzkris.usercenter.impl.tenantpackage;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.security.model.TenantLoginUser;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.usercenter.api.tenantpackage.TenantPackageInfoApi;
import com.wzkris.usercenter.domain.MenuInfoDO;
import com.wzkris.usercenter.domain.TenantInfoDO;
import com.wzkris.usercenter.domain.TenantPackageInfoDO;
import com.wzkris.usercenter.enums.MenuScopeEnum;
import com.wzkris.usercenter.mapper.*;
import com.wzkris.usercenter.response.tenantpackage.TenantPackageInfoQueryResponse;
import com.wzkris.usercenter.response.tenantpackage.TenantPackageInfoQueryResponse.BenefitItem;
import com.wzkris.usercenter.response.tenantpackage.TenantPackageInfoQueryResponse.QuotaItem;
import com.wzkris.usercenter.service.TenantPackageInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 租户套餐概览 API — 只返回业务数据，不包含任何展示元数据
 * <p>
 * 新增配额只需：
 * 1. 在 {@link #getQuotaDefinitions()} 添加一行
 * 2. 在 {@link #getQuotaLimit(TenantPackageInfoDO, String)} 添加 case
 */
@Service
@RequiredArgsConstructor
public class TenantPackageInfoApiImpl extends AbstractApi implements TenantPackageInfoApi {

    private final TenantInfoMapper tenantInfoMapper;
    private final TenantPackageInfoService tenantPackageInfoService;
    private final TenantPackageInfoMapper tenantPackageInfoMapper;
    private final MemberInfoMapper memberInfoMapper;
    private final PostInfoMapper postInfoMapper;
    private final MenuInfoMapper menuInfoMapper;

    // =============== 配额注册表 ===============

    @FunctionalInterface
    private interface QuotaSupplier {
        int count(Long tenantId);
    }

    private record QuotaDefinition(String key, QuotaSupplier supplier) {
    }

    private List<QuotaDefinition> getQuotaDefinitions() {
        return List.of(
                new QuotaDefinition("account",
                        tenantId -> Math.toIntExact(memberInfoMapper.selectCount(
                                Wrappers.lambdaQuery(com.wzkris.usercenter.domain.MemberInfoDO.class)
                                        .eq(com.wzkris.usercenter.domain.MemberInfoDO::getTenantId, tenantId)))),
                new QuotaDefinition("post",
                        tenantId -> Math.toIntExact(postInfoMapper.selectCount(
                                Wrappers.lambdaQuery(com.wzkris.usercenter.domain.PostInfoDO.class)
                                        .eq(com.wzkris.usercenter.domain.PostInfoDO::getTenantId, tenantId))))
        );
    }

    private Integer getQuotaLimit(TenantPackageInfoDO pkg, String quotaKey) {
        return switch (quotaKey) {
            case "account" -> pkg.getMemberNumLimit();
            case "post" -> pkg.getPostNumLimit();
            default -> null;
        };
    }

    // =============== 主入口 ===============

    @Override
    public Result<TenantPackageInfoQueryResponse> queryInfo() {
        TenantLoginUser loginUser = SecurityUtil.getLoginUser(TenantLoginUser.class);
        TenantInfoDO tenant = tenantInfoMapper.selectById(loginUser.getTenantId());
        if (tenant == null) {
            return requestFail("租户不存在");
        }

        TenantPackageInfoQueryResponse resp = new TenantPackageInfoQueryResponse();
        resp.setTenantName(tenant.getTenantName());
        resp.setExpireTime(tenant.getExpireTime());

        Long packageId = tenant.getPackageId();
        TenantPackageInfoDO pkg = packageId != null ? tenantPackageInfoService.getById(packageId) : null;
        boolean assigned = pkg != null;

        resp.setPackageAssigned(assigned);
        resp.setRenewalStatus(resolveRenewalStatus(assigned ? pkg.getStatus() : null, tenant.getExpireTime()));

        if (assigned) {
            resp.setPackageId(pkg.getPackageId());
            resp.setPackageName(pkg.getPackageName());
            resp.setPackageStatus(pkg.getStatus());
            resp.setPackageRemark(pkg.getRemark());
            resp.setQuotaItems(buildQuotaItems(pkg, tenant.getTenantId()));
            resp.setBenefitItems(buildBenefitItems(pkg.getPackageId()));
        }

        return ok(resp);
    }

    // =============== 配额 ===============

    private List<QuotaItem> buildQuotaItems(TenantPackageInfoDO pkg, Long tenantId) {
        List<QuotaItem> items = new ArrayList<>();
        for (QuotaDefinition def : getQuotaDefinitions()) {
            Integer limit = getQuotaLimit(pkg, def.key());
            if (limit == null) continue;
            int used = def.supplier().count(tenantId);

            QuotaItem item = new QuotaItem();
            item.setQuotaKey(def.key());
            item.setUsed(used);
            item.setLimit(limit);
            item.setUnlimited(limit == -1);
            items.add(item);
        }
        return items;
    }

    // =============== 权益 ===============

    private List<BenefitItem> buildBenefitItems(Long packageId) {
        List<Long> menuIds = tenantPackageInfoMapper.listMenuIdByPackageId(packageId);
        if (CollectionUtils.isEmpty(menuIds)) {
            return List.of();
        }
        List<MenuInfoDO> menus = menuInfoMapper.listMenuRoutes(menuIds, MenuScopeEnum.TENANT.getValue());
        if (CollectionUtils.isEmpty(menus)) {
            return List.of();
        }
        Map<String, BenefitItem> map = new LinkedHashMap<>();
        for (MenuInfoDO menu : menus) {
            if (map.containsKey(menu.getMenuName())) continue;
            BenefitItem bi = new BenefitItem();
            bi.setBenefitKey(String.valueOf(menu.getMenuId()));
            bi.setTitle(menu.getMenuName());
            map.put(menu.getMenuName(), bi);
            if (map.size() >= 8) break;
        }
        return map.values().stream().toList();
    }

    // =============== 状态解析 ===============

    private static final long EXPIRING_SOON_MILLIS = 30L * 24 * 60 * 60 * 1000;

    private String resolveRenewalStatus(String packageStatus, Date expireTime) {
        if (packageStatus != null && !"0".equals(packageStatus)) return "inactive";
        if (expireTime == null) return "permanent";
        long diff = expireTime.getTime() - System.currentTimeMillis();
        if (diff < 0) return "expired";
        if (diff <= EXPIRING_SOON_MILLIS) return "expiring_soon";
        return "active";
    }
}

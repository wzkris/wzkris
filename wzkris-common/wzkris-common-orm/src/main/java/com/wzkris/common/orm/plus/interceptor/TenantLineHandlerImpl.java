package com.wzkris.common.orm.plus.interceptor;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.wzkris.common.orm.plus.config.TenantProperties;
import com.wzkris.common.orm.utils.DynamicTenantUtil;
import com.wzkris.common.security.utils.LoginUtil;
import lombok.AllArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 租户权限处理器
 * @date : 2024/3/26 14:32
 */
@AllArgsConstructor
public class TenantLineHandlerImpl implements TenantLineHandler {

    private final TenantProperties tenantProperties;

    @Override
    public Expression getTenantId() {
        // 若设置了动态租户则走动态租户
        Long tenantId = DynamicTenantUtil.get();
        if (tenantId != null) {
            return new LongValue(tenantId);
        }
        // 没设置动态租户走自身的租户
        tenantId = LoginUtil.getTenantId();
        return new LongValue(tenantId);
    }

    @Override
    public boolean ignoreTable(String tableName) {
        // 若设置了动态租户或已登录则必须走拦截
        if (DynamicTenantUtil.get() != null
                || LoginUtil.isLogin()) {
            return this.isIgnoreTable(tableName);
        }
        // 未登录则忽略
        return true;
    }

    // 是否忽略表名
    private boolean isIgnoreTable(String tableName) {
        return !tenantProperties.getIncludes().contains(tableName);
    }
}

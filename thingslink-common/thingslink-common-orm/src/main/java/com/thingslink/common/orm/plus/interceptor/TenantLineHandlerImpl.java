package com.thingslink.common.orm.plus.interceptor;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.thingslink.common.orm.plus.config.TenantProperties;
import com.thingslink.common.orm.utils.DynamicTenantUtil;
import com.thingslink.common.security.utils.SysUtil;
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
        tenantId = SysUtil.getTenantId();
        return new LongValue(tenantId);
    }

    @Override
    public boolean ignoreTable(String tableName) {
        // 若设置了动态租户则必须走拦截
        if (DynamicTenantUtil.get() != null) {
            return this.isIgnoreTable(tableName);
        }
        // 未登录则忽略
        if (!SysUtil.isLogin()) {
            return true;
        }
        // 最后再去判断表名是否忽略
        return this.isIgnoreTable(tableName);
    }

    // 是否忽略表名
    private boolean isIgnoreTable(String tableName) {
        return !tenantProperties.getIncludes().contains(tableName);
    }
}

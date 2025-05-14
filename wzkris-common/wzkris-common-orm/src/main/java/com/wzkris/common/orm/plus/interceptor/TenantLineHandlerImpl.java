package com.wzkris.common.orm.plus.interceptor;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.wzkris.common.orm.plus.config.TenantProperties;
import com.wzkris.common.orm.utils.DynamicTenantUtil;
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
        return new LongValue(DynamicTenantUtil.get());// 忽略此警告，走到此必不为空
    }

    @Override
    public boolean ignoreTable(String tableName) {
        if (DynamicTenantUtil.get() == null) {
            return true;
        }
        return !tenantProperties.getIncludes().contains(tableName);
    }

    // 是否忽略表名
    private boolean isIgnoreTable(String tableName) {
        return !tenantProperties.getIncludes().contains(tableName);
    }
}

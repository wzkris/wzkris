package com.wzkris.common.orm.plus.interceptor;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.wzkris.common.core.context.UserContextProvider;
import com.wzkris.common.orm.plus.config.TenantProperties;
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

    private final UserContextProvider userContextProvider;

    @Override
    public Expression getTenantId() {
        Long tenantId = userContextProvider.getTenantId();
        if (tenantId == null) {
            throw new IllegalStateException("租户上下文缺失，无法解析 tenantId");
        }
        return new LongValue(tenantId);
    }

    @Override
    public boolean ignoreTable(String tableName) {
        Long tenantId = userContextProvider.getTenantId();
        return tenantId == null || !tenantProperties.getIncludes().contains(tableName);
    }

}

package com.wzkris.common.orm.plus.interceptor;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.wzkris.common.orm.plus.config.TenantProperties;
import com.wzkris.common.security.utils.LoginStaffUtil;
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
        return new LongValue(LoginStaffUtil.getTenantId());
    }

    @Override
    public boolean ignoreTable(String tableName) {
        return !LoginStaffUtil.isLogin() || !tenantProperties.getIncludes().contains(tableName);
    }

}

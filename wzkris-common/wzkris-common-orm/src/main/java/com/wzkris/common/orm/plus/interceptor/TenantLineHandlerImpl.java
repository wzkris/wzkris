package com.wzkris.common.orm.plus.interceptor;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.wzkris.common.core.support.LoginUser;
import com.wzkris.common.core.support.UserContextHelper;
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

    private final UserContextHelper userContextHelper;

    @Override
    public Expression getTenantId() {
        LoginUser loginUser = userContextHelper.getLoginUser();
        return new LongValue(loginUser.getTenantId());
    }

    @Override
    public boolean ignoreTable(String tableName) {
        LoginUser loginUser = userContextHelper.getLoginUser();
        return loginUser == null || loginUser.getTenantId() == null
                || !tenantProperties.getIncludes().contains(tableName);
    }

}

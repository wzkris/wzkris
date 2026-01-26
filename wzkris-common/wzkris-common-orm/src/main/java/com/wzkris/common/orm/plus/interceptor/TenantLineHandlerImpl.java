package com.wzkris.common.orm.plus.interceptor;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.orm.plus.config.TenantProperties;
import com.wzkris.common.security.utils.SecurityUtil;
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
        return new LongValue(SecurityUtil.getTenantId());
    }

    @Override
    public boolean ignoreTable(String tableName) {
        return !SecurityUtil.isAuth(AuthTypeEnum.TENANT) || !tenantProperties.getIncludes().contains(tableName);
    }

}

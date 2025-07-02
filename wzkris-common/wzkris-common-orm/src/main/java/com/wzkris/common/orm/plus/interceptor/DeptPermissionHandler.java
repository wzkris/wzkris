package com.wzkris.common.orm.plus.interceptor;

import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import com.wzkris.common.orm.utils.DeptScopeUtil;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 部门权限处理器
 * @date : 2024/1/11 14:32
 */
public class DeptPermissionHandler implements MultiDataPermissionHandler {

    @Override
    public Expression getSqlSegment(Table table, Expression where, String mappedStatementId) {
        return DeptScopeUtil.getSqlExpression();
    }

}

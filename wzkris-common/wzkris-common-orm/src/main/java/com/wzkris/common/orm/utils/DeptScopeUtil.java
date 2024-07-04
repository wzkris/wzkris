package com.wzkris.common.orm.utils;

import com.alibaba.ttl.TransmittableThreadLocal;
import net.sf.jsqlparser.expression.Expression;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 部门权限工具
 * @date : 2023/12/11 10:54
 */
public class DeptScopeUtil {
    // sql表达式片段
    private static final ThreadLocal<Expression> LOCAL_DATA_AUTH_SQL = new TransmittableThreadLocal<>();

    public static Expression getSqlExpression() {
        return LOCAL_DATA_AUTH_SQL.get();
    }

    public static void setSqlExpression(Expression expression) {
        LOCAL_DATA_AUTH_SQL.set(expression);
    }

    public static void clear() {
        LOCAL_DATA_AUTH_SQL.remove();
    }
}

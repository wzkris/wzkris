package com.wzkris.common.orm.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.sf.jsqlparser.expression.Expression;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 部门权限工具
 * @date : 2023/12/11 10:54
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeptScopeUtil {

    private static final ThreadLocal<Expression> LOCAL_DATA_AUTH_SQL = new ThreadLocal<>();

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

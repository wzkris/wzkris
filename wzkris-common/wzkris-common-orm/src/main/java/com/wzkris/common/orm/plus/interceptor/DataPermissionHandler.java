package com.wzkris.common.orm.plus.interceptor;

import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.annotation.DataColumn;
import com.wzkris.common.orm.annotation.DataScope;
import com.wzkris.common.orm.utils.DataScopeUtil;
import com.wzkris.common.security.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.BooleanValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.Objects;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 数据权限处理器
 * @date : 2024/1/11 14:32
 */
@Slf4j
public class DataPermissionHandler implements MultiDataPermissionHandler {

    @Override
    public Expression getSqlSegment(Table table, Expression where, String mappedStatementId) {
        DataScope dataScope = DataScopeUtil.getDataScope(mappedStatementId);
        if (dataScope == null) {
            return null;
        }

        // 超级管理员跳过所有数据权限
        if (SecurityUtil.isSuperAdmin()) {
            return null;
        }

        return handleDataScope(mappedStatementId, dataScope);
    }

    private Expression handleDataScope(String mappedStatementId, DataScope dataScope) {
        Expression resultExpression = null;

        for (DataColumn dataColumn : dataScope.value()) {
            String column = StringUtil.isBlank(dataColumn.alias())
                    ? dataColumn.column()
                    : dataColumn.alias() + StringUtil.DOT + dataColumn.column();
            Object value = DataScopeUtil.getParameter(column);
            if (Objects.isNull(value)) {
                log.warn("method: {}, didn't put parameter: {}", mappedStatementId, column);
                continue;
            }

            Expression currentExpression = handleExpression(column, value);

            if (resultExpression == null) {
                resultExpression = currentExpression;
            } else {
                resultExpression = new AndExpression(resultExpression, currentExpression);
            }
        }

        if (resultExpression == null) {
            return new BooleanValue(false);
        }

        return resultExpression;
    }

    private Expression handleExpression(String column, Object value) {
        if (value instanceof Collection<?> collection) {
            return handleCollectionParameter(column, collection);
        }
        return new EqualsTo(new Column(column), handleSingleParameter(value));
    }

    private Expression handleCollectionParameter(String column, Collection<?> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return new BooleanValue(false);
        }
        InExpression inExpression = new InExpression();
        inExpression.setLeftExpression(new Column(column));
        ParenthesedExpressionList<Expression> expressions = new ParenthesedExpressionList<>();
        for (Object val : collection) {
            expressions.add(handleSingleParameter(val));
        }
        inExpression.setRightExpression(expressions);
        return inExpression;
    }

    private Expression handleSingleParameter(Object value) {
        if (value instanceof Long longValue) {
            return new LongValue(longValue);
        } else if (value instanceof Integer intValue) {
            return new LongValue(intValue.longValue());
        } else if (value instanceof Short shortValue) {
            return new LongValue(shortValue.longValue());
        } else if (value instanceof Byte byteValue) {
            return new LongValue(byteValue.longValue());
        }
        return new StringValue(value.toString());
    }

}

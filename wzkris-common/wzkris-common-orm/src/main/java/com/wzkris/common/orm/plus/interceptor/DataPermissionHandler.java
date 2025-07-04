package com.wzkris.common.orm.plus.interceptor;

import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.annotation.DataColumn;
import com.wzkris.common.orm.annotation.DataScope;
import com.wzkris.common.orm.utils.DataScopeUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.BooleanValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
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
        if (dataScope != null) {
            return handleDataScope(mappedStatementId, dataScope);
        }
        return null;
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

            // 组合表达式，使用 AND 连接
            if (resultExpression == null) {
                resultExpression = currentExpression;
            } else {
                resultExpression = new AndExpression(resultExpression, currentExpression);
            }
        }

        return resultExpression;
    }

    private Expression handleExpression(String column, Object value) {
        Expression expression;
        if (value instanceof Collection<?> collection) {
            if (CollectionUtils.isEmpty(collection)) {
                expression = new BooleanValue(false);
            } else {
                expression = handleCollectionParameter(column, collection);
            }
        } else {
            expression = handleSingleParameter(value);
        }
        return expression;
    }

    private InExpression handleCollectionParameter(String column, Collection<?> collection) {
        InExpression inExpression = new InExpression();
        inExpression.setLeftExpression(new Column(column));
        ParenthesedExpressionList<Expression> expressions = new ParenthesedExpressionList<>();
        for (Object val : collection) {
            Expression exp = handleSingleParameter(val);
            expressions.add(exp);
        }
        inExpression.setRightExpression(expressions);
        return inExpression;
    }

    private Expression handleSingleParameter(Object value) {
        Expression expression;
        if (value instanceof Long longValue) {
            expression = new LongValue(longValue);
        } else if (value instanceof Integer intValue) {
            expression = new LongValue(intValue.longValue());
        } else if (value instanceof Short shortValue) {
            expression = new LongValue(shortValue.longValue());
        } else if (value instanceof Byte byteValue) {
            expression = new LongValue(byteValue.longValue());
        } else {
            expression = new StringValue(value.toString());
        }
        return expression;
    }

}

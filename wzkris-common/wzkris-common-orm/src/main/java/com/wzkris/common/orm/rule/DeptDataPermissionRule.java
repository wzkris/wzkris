package com.wzkris.common.orm.rule;

import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.security.model.LoginAdminUser;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;

/**
 * 部门数据权限规则
 * <p>
 * 管理员体系下按 deptScopes 过滤，单值用 =，多值用 IN。
 *
 * @author wzkris
 */
@Slf4j
public class DeptDataPermissionRule implements DataPermissionRule {

    @Override
    public DataPermissionType getType() {
        return DataPermissionType.DEPT;
    }

    @Override
    public boolean isApplicable(BaseLoginUser loginUser) {
        if (!(loginUser instanceof LoginAdminUser adminUser)) {
            return false;
        }
        return CollectionUtils.isNotEmpty(adminUser.getDeptScopes());
    }

    @Override
    public Expression getExpression(Table table, Expression where,
                                     DataColumnConfig config, BaseLoginUser loginUser) {
        LoginAdminUser adminUser = (LoginAdminUser) loginUser;
        String column = config.getFullColumn();
        Collection<?> deptScopes = adminUser.getDeptScopes();

        if (CollectionUtils.isEmpty(deptScopes)) {
            log.warn("DeptDataPermissionRule: deptScopes is empty for user [{}]", loginUser.getName());
            return null;
        }

        if (deptScopes.size() == 1) {
            Object val = deptScopes.iterator().next();
            return new EqualsTo(new Column(column), toExpression(val));
        }

        InExpression inExpression = new InExpression();
        inExpression.setLeftExpression(new Column(column));
        ParenthesedExpressionList<Expression> expressions = new ParenthesedExpressionList<>();
        for (Object val : deptScopes) {
            expressions.add(toExpression(val));
        }
        inExpression.setRightExpression(expressions);
        return inExpression;
    }

    private net.sf.jsqlparser.expression.Expression toExpression(Object val) {
        if (val instanceof Number num) {
            return new LongValue(num.longValue());
        }
        return new net.sf.jsqlparser.expression.StringValue(val.toString());
    }

}

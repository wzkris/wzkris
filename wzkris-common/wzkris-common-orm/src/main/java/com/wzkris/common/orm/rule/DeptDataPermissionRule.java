package com.wzkris.common.orm.rule;

import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.model.DataIdentity;
import com.wzkris.common.core.model.UserRole;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 部门数据权限规则
 *
 * @author wzkris
 */
@Slf4j
public class DeptDataPermissionRule implements DataPermissionRule {

    private static final String DATA_SCOPE_ALL = "1";

    @Override
    public DataPermissionType getType() {
        return DataPermissionType.DEPT;
    }

    @Override
    public boolean isApplicable(BaseLoginUser loginUser) {
        List<UserRole> roles = loginUser.getRoles();
        if (CollectionUtils.isEmpty(roles)) {
            return true;
        }
        return roles.stream().noneMatch(r -> DATA_SCOPE_ALL.equals(r.getDataScope()));
    }

    @Override
    public Expression getExpression(Table table, Expression where,
                                     DataColumnConfig config, BaseLoginUser loginUser) {
        List<UserRole> roles = loginUser.getRoles();
        if (CollectionUtils.isEmpty(roles)) {
            return null;
        }

        Set<Long> deptIds = roles.stream()
                .filter(r -> !DATA_SCOPE_ALL.equals(r.getDataScope()))
                .map(UserRole::getDataIdentityList)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .map(DataIdentity::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (deptIds.isEmpty()) {
            return null;
        }

        String column = config.getFullColumn();
        if (deptIds.size() == 1) {
            return new EqualsTo(new Column(column), new LongValue(deptIds.iterator().next()));
        }

        InExpression inExpression = new InExpression();
        inExpression.setLeftExpression(new Column(column));
        ParenthesedExpressionList<Expression> expressions = new ParenthesedExpressionList<>();
        for (Long id : deptIds) {
            expressions.add(new LongValue(id));
        }
        inExpression.setRightExpression(expressions);
        return inExpression;
    }

}

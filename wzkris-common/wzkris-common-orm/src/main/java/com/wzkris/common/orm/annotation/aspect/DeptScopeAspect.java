package com.wzkris.common.orm.annotation.aspect;

import cn.hutool.core.collection.CollUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.annotation.DeptScope;
import com.wzkris.common.orm.utils.DeptScopeUtil;
import com.wzkris.common.security.utils.SysUtil;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 部门权限切面
 * @date : 2023/12/11 10:56
 */
@Aspect
public class DeptScopeAspect {

    /**
     * 方法执行前执行
     */
    @Before("@annotation(deptScope)")
    public void before(JoinPoint point, DeptScope deptScope) {
        this.handleDataScope(deptScope);
    }

    /**
     * 方法执行后销毁数据权限sql
     */
    @After("@annotation(deptScope)")
    public void after(JoinPoint point, DeptScope deptScope) {
        DeptScopeUtil.clear();
    }

    /**
     * 处理部门数据权限
     */
    private void handleDataScope(DeptScope deptScope) {
        if (!SysUtil.isLogin()) {
            return;
        }
        // 租户的最高管理员不查询部门数据权限
        if (SysUtil.isAdministrator()) {
            return;
        }

        // 生成权限sql片段
        String aliasColumn = StringUtil.isBlank(deptScope.tableAlias()) ? deptScope.columnAlias() :
                StringUtil.format("{}.{}", deptScope.tableAlias(), deptScope.columnAlias());
        Expression expression;
        List<Long> deptScopes = SysUtil.getLoginSyser().getDeptScopes();
        if (CollUtil.isEmpty(deptScopes)) {
            // 没有部门权限数据则直接拼接-1, 查不出来即可
            expression = new ExpressionList<>(new LongValue(-1L));
        }
        else {
            expression = new ExpressionList<>(deptScopes.stream().map(LongValue::new).collect(Collectors.toList()));
        }
        InExpression inExpression = new InExpression(new Column(aliasColumn), expression);

        DeptScopeUtil.setSqlExpression(inExpression);
    }

}

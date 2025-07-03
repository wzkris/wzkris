package com.wzkris.common.orm.annotation.aspect;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.annotation.DeptScope;
import com.wzkris.common.orm.utils.DeptScopeUtil;
import com.wzkris.common.security.utils.SystemUserUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.schema.Column;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 部门权限切面
 * @date : 2023/12/11 10:56
 */
@Slf4j
@Aspect
public class DeptScopeAspect {

    private static final ConcurrentSkipListSet<String> WHITE_SET = new ConcurrentSkipListSet<>();

    @Pointcut("@annotation(com.wzkris.common.orm.annotation.DeptScope)")
    public void pointcut() {
    }

    /**
     * 方法执行前执行
     */
    @Before("pointcut()")
    public void before(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        String methodName = signature.getDeclaringTypeName()
                + StringPool.HASH
                + signature.getMethod().getName();

        DeptScope deptScope = AnnotationUtils.findAnnotation(signature.getMethod(), DeptScope.class);

        if (WHITE_SET.contains(methodName)) {
            handleDataScope(deptScope);
        } else {
            Class<?> clazz = signature.getDeclaringType();
            if (!clazz.isInterface()) {
                log.warn("部门权限注解只允许标记在接口内，忽略当前方法：'{}'的权限执行", methodName);
                return;
            }
            WHITE_SET.add(methodName);
            handleDataScope(deptScope);
        }
    }

    /**
     * 方法执行后销毁数据权限sql
     */
    @After("pointcut()")
    public void after() {
        DeptScopeUtil.clear();
    }

    /**
     * 处理部门数据权限
     */
    private void handleDataScope(DeptScope deptScope) {
        if (!SystemUserUtil.isLogin()) {
            return;
        }
        // 租户的最高管理员不查询部门数据权限
        if (SystemUserUtil.isAdmin()) {
            return;
        }

        // 生成权限sql片段
        String aliasColumn = StringUtil.isBlank(deptScope.tableAlias())
                ? deptScope.columnAlias()
                : String.format("%s.%s", deptScope.tableAlias(), deptScope.columnAlias());
        List<Long> deptScopes = SystemUserUtil.getUser().getDeptScopes();

        Expression expression;
        if (CollectionUtils.isEmpty(deptScopes)) {
            // 没有部门权限数据则直接拼接-1, 查不出来即可
            expression = new ParenthesedExpressionList<>(new LongValue(-1L));
        } else {
            expression = new ParenthesedExpressionList<>(
                    deptScopes.stream().map(LongValue::new).collect(Collectors.toList()));
        }
        InExpression inExpression = new InExpression(new Column(aliasColumn), expression);

        DeptScopeUtil.setSqlExpression(inExpression);
    }

}

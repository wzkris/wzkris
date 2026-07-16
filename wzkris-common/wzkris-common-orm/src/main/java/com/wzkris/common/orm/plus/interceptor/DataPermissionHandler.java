package com.wzkris.common.orm.plus.interceptor;

import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import com.wzkris.common.core.model.RoleContext;
import com.wzkris.common.orm.annotation.DataPermission;
import com.wzkris.common.orm.annotation.DataScope;
import com.wzkris.common.orm.rule.DataColumnConfig;
import com.wzkris.common.orm.rule.DataPermissionRule;
import com.wzkris.common.orm.rule.DataPermissionType;
import com.wzkris.common.security.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.BooleanValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.schema.Table;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 数据权限处理器
 * <p>
 * 策略模式驱动：通过反射解析 {@link DataScope} 注解，
 * 将每条 {@link DataPermission} 委托给对应的 {@link DataPermissionRule} 生成 SQL 表达式。
 * <p>
 * 支持多表感知：当 {@link DataPermission#alias()} 指定表别名时，
 * 仅对 SQL 中别名匹配的表生成条件，避免 JOIN 查询中条件重复拼接。
 *
 * @author wzkris
 */
@Slf4j
public class DataPermissionHandler implements MultiDataPermissionHandler {

    /**
     * 注解缓存中代表"无注解"的哨兵，避免 ConcurrentHashMap 不支持 null 值
     */
    private static final DataScope EMPTY_SCOPE = new DataScope() {
        @Override
        public DataPermission[] value() {
            return new DataPermission[0];
        }

        @Override
        public Class<? extends java.lang.annotation.Annotation> annotationType() {
            return DataScope.class;
        }
    };

    private final Map<DataPermissionType, DataPermissionRule> ruleMap;

    private final Map<String, DataScope> annotationCache = new ConcurrentHashMap<>();

    public DataPermissionHandler(List<DataPermissionRule> rules) {
        this.ruleMap = rules.stream()
                .collect(Collectors.toMap(DataPermissionRule::getType, r -> r));
        log.info("DataPermissionHandler initialized with rules: {}", ruleMap.keySet());
    }

    @Override
    public Expression getSqlSegment(Table table, Expression where, String mappedStatementId) {
        DataScope dataScope = resolveAnnotation(mappedStatementId);
        if (dataScope == null || dataScope.value().length == 0) {
            return null;
        }

        RoleContext roleContext = SecurityUtil.getRoleContext();

        Expression result = null;
        boolean hasMatchedRule = false;

        for (DataPermission dp : dataScope.value()) {
            if (!matchesTable(dp, table)) {
                continue;
            }

            DataPermissionRule rule = ruleMap.get(dp.type());
            if (rule == null) {
                log.warn("No DataPermissionRule found for type: {}", dp.type());
                continue;
            }

            if (!rule.isApplicable(roleContext)) {
                continue;
            }

            hasMatchedRule = true;
            DataColumnConfig config = DataColumnConfig.of(dp);
            Expression expr = rule.getExpression(table, where, config, roleContext);
            if (expr == null) {
                continue;
            }

            result = (result == null) ? expr : new AndExpression(result, expr);
        }

        // 有匹配当前表的规则但无表达式生成 -> 拦截（安全优先）
        if (result == null && hasMatchedRule) {
            return new BooleanValue(false);
        }

        return result;
    }

    /**
     * 判断注解配置的 alias 是否匹配当前解析的表
     * <p>
     * - alias 为空：匹配所有表（单表查询场景）
     * - alias 不为空：仅匹配 SQL 中别名相同的表（JOIN 查询场景）
     */
    private boolean matchesTable(DataPermission dp, Table table) {
        String alias = dp.alias();
        if (alias == null || alias.isBlank()) {
            return true;
        }
        Alias tableAlias = table.getAlias();
        return tableAlias != null && alias.equals(tableAlias.getName());
    }

    /**
     * 通过 mappedStatementId 反射获取 @DataScope 注解，带缓存
     */
    private DataScope resolveAnnotation(String mappedStatementId) {
        return annotationCache.computeIfAbsent(mappedStatementId, id -> {
            DataScope scope = doResolveAnnotation(id);
            return scope != null ? scope : EMPTY_SCOPE;
        });
    }

    private DataScope doResolveAnnotation(String mappedStatementId) {
        int lastDot = mappedStatementId.lastIndexOf('.');
        if (lastDot < 0) {
            return null;
        }

        String className = mappedStatementId.substring(0, lastDot);
        String methodName = mappedStatementId.substring(lastDot + 1);

        Class<?> mapperClass;
        try {
            mapperClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }

        // 优先方法级注解
        for (Method method : mapperClass.getMethods()) {
            if (method.getName().equals(methodName)) {
                DataScope methodScope = AnnotatedElementUtils.findMergedAnnotation(method, DataScope.class);
                if (methodScope != null) {
                    return methodScope;
                }
            }
        }

        // 回退类级注解
        return AnnotatedElementUtils.findMergedAnnotation(mapperClass, DataScope.class);
    }

}

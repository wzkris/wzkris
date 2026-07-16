package com.wzkris.common.orm.rule;

import com.wzkris.common.core.model.RoleContext;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;

/**
 * 数据权限规则策略接口
 *
 * @author wzkris
 */
public interface DataPermissionRule {

    DataPermissionType getType();

    /**
     * 当前用户是否适用此规则
     *
     * @param roleContext 角色上下文
     * @return 是否适用
     */
    boolean isApplicable(RoleContext roleContext);

    /**
     * 生成 SQL 表达式
     *
     * @param table       当前解析的表
     * @param where       原始 where 条件
     * @param config      列配置
     * @param roleContext 角色上下文
     * @return SQL 表达式，返回 null 表示跳过
     */
    Expression getExpression(Table table, Expression where, DataColumnConfig config, RoleContext roleContext);

}
